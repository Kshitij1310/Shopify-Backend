package service;

import dto.*;
import model.OrderRecord;
import model.Product;
import repository.OrderRepository;
import repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class SellerDashboardService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public SellerDashboardService(OrderRepository orderRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    public SellerDashboardSnapshot generateDashboardSnapshot(String sellerId) {
        if (sellerId == null || sellerId.isBlank()) {
            throw new IllegalArgumentException("Seller ID cannot be empty.");
        }

        List<Product> sellerProducts = productRepository.findAll().stream()
                .filter(product -> sellerId.equals(product.getSellerId()))
                .collect(Collectors.toList());

        List<OrderRecord> sellerOrders = orderRepository.findAll().stream()
                .filter(order -> isSellerInOrder(order, sellerId))
                .collect(Collectors.toList());

        long now = System.currentTimeMillis();
        long todayStart = LocalDate.now(ZoneOffset.UTC).atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli();

        SellerDashboardSnapshot snapshot = new SellerDashboardSnapshot();
        snapshot.setSellerId(sellerId);
        snapshot.setTotalOrders(sellerOrders.size());
        snapshot.setOrdersToday(sellerOrders.stream().filter(o -> o.getCreatedAt() != null && o.getCreatedAt() >= todayStart).count());

        snapshot.setSuccessfulPaymentsCount(countByPaymentStatus(sellerOrders, "success"));
        snapshot.setPendingPaymentsCount(countByPaymentStatus(sellerOrders, "pending"));
        snapshot.setFailedPaymentsCount(countByPaymentStatus(sellerOrders, "failed"));
        snapshot.setCanceledOrdersCount(sellerOrders.stream().filter(o -> "canceled".equalsIgnoreCase(o.getStatus())).count());

        BigDecimal totalRevenue = sellerOrders.stream()
                .filter(o -> isSuccessfulPayment(o))
                .map(this::amountOf)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal todayRevenue = sellerOrders.stream()
                .filter(o -> isSuccessfulPayment(o) && o.getCreatedAt() != null && o.getCreatedAt() >= todayStart)
                .map(this::amountOf)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal pendingRevenue = sellerOrders.stream()
                .filter(o -> "pending".equalsIgnoreCase(normalize(o.getPaymentStatus())))
                .map(this::amountOf)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        snapshot.setTotalRevenue(totalRevenue);
        snapshot.setTodayRevenue(todayRevenue);
        snapshot.setPendingRevenue(pendingRevenue);
        long successfulOrders = Math.max(1L, snapshot.getSuccessfulPaymentsCount());
        snapshot.setAverageOrderValue(totalRevenue.divide(BigDecimal.valueOf(successfulOrders), 2, RoundingMode.HALF_UP));
        snapshot.setTotalUnitsSold(sellerOrders.stream()
                .mapToLong(order -> estimateUnits(order.getItemsJson()))
            .sum());

        snapshot.setPaymentSuccessRate(percent(snapshot.getSuccessfulPaymentsCount(), sellerOrders.size()));
        snapshot.setPaymentFailureRate(percent(snapshot.getFailedPaymentsCount(), sellerOrders.size()));
        snapshot.setPendingRiskShare(percent(snapshot.getPendingPaymentsCount(), sellerOrders.size()));

        snapshot.setLowStockProductsCount(sellerProducts.stream().filter(p -> p.getQuantity() != null && p.getQuantity() <= 5).count());
        snapshot.setProductsMissingImage(sellerProducts.stream().filter(p -> p.getImageUrl() == null || p.getImageUrl().isBlank()).count());
        snapshot.setStaleProducts(sellerProducts.stream().filter(p -> p.getUpdatedAt() != null && p.getUpdatedAt() < now - 7L * 24 * 60 * 60 * 1000).count());
        snapshot.setCatalogQualityRate(percent(sellerProducts.stream().filter(p -> p.getImageUrl() != null && !p.getImageUrl().isBlank()).count(), sellerProducts.size()));

        snapshot.setRevenueTrend(buildRevenueTrend(sellerOrders));
        snapshot.setOrdersDistribution(buildOrdersDistribution(sellerOrders));
        snapshot.setCategoryDistribution(buildCategoryDistribution(sellerProducts));
        snapshot.setRecentOrders(buildRecentOrders(sellerOrders));
        snapshot.setTopProducts(buildTopProducts(sellerProducts));
        return snapshot;
    }

    private boolean isSellerInOrder(OrderRecord order, String sellerId) {
        String json = order.getSellerIdsJson();
        return json != null && json.contains("\"" + sellerId + "\"");
    }

    private boolean isSuccessfulPayment(OrderRecord order) {
        return "success".equalsIgnoreCase(normalize(order.getPaymentStatus()));
    }

    private long countByPaymentStatus(List<OrderRecord> orders, String status) {
        return orders.stream().filter(o -> status.equalsIgnoreCase(normalize(o.getPaymentStatus()))).count();
    }

    private BigDecimal amountOf(OrderRecord order) {
        return order.getAmount() == null ? BigDecimal.ZERO : order.getAmount();
    }

    private BigDecimal percent(long numerator, long denominator) {
        if (denominator <= 0) return BigDecimal.ZERO;
        return BigDecimal.valueOf(numerator * 100.0 / denominator).setScale(2, RoundingMode.HALF_UP);
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim().toLowerCase();
    }

    private List<SellerRevenueTrendPoint> buildRevenueTrend(List<OrderRecord> orders) {
        long todayStart = LocalDate.now(ZoneOffset.UTC).atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli();
        Map<Long, BigDecimal> totals = new java.util.LinkedHashMap<>();
        for (int day = 6; day >= 0; day--) {
            long bucket = todayStart - day * 24L * 60L * 60L * 1000L;
            totals.put(bucket, BigDecimal.ZERO);
        }
        for (OrderRecord order : orders) {
            if (!isSuccessfulPayment(order) || order.getCreatedAt() == null) continue;
            long dayBucket = LocalDate.ofInstant(Instant.ofEpochMilli(order.getCreatedAt()), ZoneOffset.UTC)
                    .atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli();
            if (totals.containsKey(dayBucket)) {
                totals.put(dayBucket, totals.get(dayBucket).add(amountOf(order)));
            }
        }
        List<SellerRevenueTrendPoint> points = new ArrayList<>();
        totals.entrySet().stream().sorted(Map.Entry.comparingByKey()).forEach(entry -> {
            SellerRevenueTrendPoint point = new SellerRevenueTrendPoint();
            point.setDayStartUtcMs(entry.getKey());
            point.setRevenue(entry.getValue());
            points.add(point);
        });
        return points;
    }

    private SellerOrdersDistribution buildOrdersDistribution(List<OrderRecord> orders) {
        SellerOrdersDistribution distribution = new SellerOrdersDistribution();
        distribution.setSuccess(countByPaymentStatus(orders, "success"));
        distribution.setPending(countByPaymentStatus(orders, "pending"));
        distribution.setFailed(countByPaymentStatus(orders, "failed"));
        distribution.setCanceled(orders.stream().filter(o -> "canceled".equalsIgnoreCase(normalize(o.getStatus()))).count());
        return distribution;
    }

    private List<SellerCategoryDistributionItem> buildCategoryDistribution(List<Product> products) {
        Map<String, Long> counts = products.stream()
                .collect(Collectors.groupingBy(product -> product.getCategory() == null ? "Uncategorized" : product.getCategory(), Collectors.counting()));
        return counts.entrySet().stream()
                .map(entry -> {
                    SellerCategoryDistributionItem item = new SellerCategoryDistributionItem();
                    item.setCategory(entry.getKey());
                    item.setCount(entry.getValue());
                    return item;
                })
                .sorted(Comparator.comparingLong(SellerCategoryDistributionItem::getCount).reversed())
                .collect(Collectors.toList());
    }

    private List<SellerRecentOrder> buildRecentOrders(List<OrderRecord> orders) {
        return orders.stream()
                .sorted(Comparator.comparing(OrderRecord::getCreatedAt, Comparator.nullsLast(Long::compareTo)).reversed())
                .limit(6)
                .map(order -> {
                    SellerRecentOrder recentOrder = new SellerRecentOrder();
                    recentOrder.setId(order.getId() == null ? null : order.getId().toString());
                    recentOrder.setCreatedAt(order.getCreatedAt() == null ? 0L : order.getCreatedAt());
                    recentOrder.setAmount(amountOf(order));
                    recentOrder.setStatus(order.getStatus());
                    recentOrder.setPaymentStatus(order.getPaymentStatus());
                    return recentOrder;
                })
                .collect(Collectors.toList());
    }

    private List<SellerTopProduct> buildTopProducts(List<Product> products) {
        return products.stream()
                .map(product -> {
                    SellerTopProduct topProduct = new SellerTopProduct();
                    topProduct.setId(product.getId() == null ? null : product.getId().toString());
                    topProduct.setName(product.getName());
                    topProduct.setRevenue(BigDecimal.valueOf((product.getPrice() == null ? 0.0 : product.getPrice()) * (product.getQuantity() == null ? 0 : product.getQuantity())));
                    return topProduct;
                })
                .sorted(Comparator.comparing(SellerTopProduct::getRevenue).reversed())
                .limit(5)
                .collect(Collectors.toList());
    }

    private long estimateUnits(String itemsJson) {
        if (itemsJson == null || itemsJson.isBlank()) {
            return 0L;
        }
        Pattern pattern = Pattern.compile("\\\"quantity\\\"\\s*:\\s*(\\d+)");
        Matcher matcher = pattern.matcher(itemsJson);
        long total = 0L;
        while (matcher.find()) {
            total += Long.parseLong(matcher.group(1));
        }
        return total;
    }
}
