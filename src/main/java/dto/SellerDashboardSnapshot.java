package dto;

import java.math.BigDecimal;
import java.util.List;

public class SellerDashboardSnapshot {
    private String sellerId;
    private long totalOrders;
    private long ordersToday;
    private long successfulPaymentsCount;
    private long pendingPaymentsCount;
    private long failedPaymentsCount;
    private long canceledOrdersCount;
    private BigDecimal totalRevenue = BigDecimal.ZERO;
    private BigDecimal todayRevenue = BigDecimal.ZERO;
    private BigDecimal pendingRevenue = BigDecimal.ZERO;
    private BigDecimal averageOrderValue = BigDecimal.ZERO;
    private long totalUnitsSold;
    private BigDecimal paymentSuccessRate = BigDecimal.ZERO;
    private BigDecimal paymentFailureRate = BigDecimal.ZERO;
    private BigDecimal catalogQualityRate = BigDecimal.ZERO;
    private BigDecimal pendingRiskShare = BigDecimal.ZERO;
    private long lowStockProductsCount;
    private long productsMissingImage;
    private long staleProducts;
    private List<SellerRevenueTrendPoint> revenueTrend;
    private SellerOrdersDistribution ordersDistribution;
    private List<SellerCategoryDistributionItem> categoryDistribution;
    private List<SellerRecentOrder> recentOrders;
    private List<SellerTopProduct> topProducts;

    public String getSellerId() { return sellerId; }
    public void setSellerId(String sellerId) { this.sellerId = sellerId; }
    public long getTotalOrders() { return totalOrders; }
    public void setTotalOrders(long totalOrders) { this.totalOrders = totalOrders; }
    public long getOrdersToday() { return ordersToday; }
    public void setOrdersToday(long ordersToday) { this.ordersToday = ordersToday; }
    public long getSuccessfulPaymentsCount() { return successfulPaymentsCount; }
    public void setSuccessfulPaymentsCount(long successfulPaymentsCount) { this.successfulPaymentsCount = successfulPaymentsCount; }
    public long getPendingPaymentsCount() { return pendingPaymentsCount; }
    public void setPendingPaymentsCount(long pendingPaymentsCount) { this.pendingPaymentsCount = pendingPaymentsCount; }
    public long getFailedPaymentsCount() { return failedPaymentsCount; }
    public void setFailedPaymentsCount(long failedPaymentsCount) { this.failedPaymentsCount = failedPaymentsCount; }
    public long getCanceledOrdersCount() { return canceledOrdersCount; }
    public void setCanceledOrdersCount(long canceledOrdersCount) { this.canceledOrdersCount = canceledOrdersCount; }
    public BigDecimal getTotalRevenue() { return totalRevenue; }
    public void setTotalRevenue(BigDecimal totalRevenue) { this.totalRevenue = totalRevenue; }
    public BigDecimal getTodayRevenue() { return todayRevenue; }
    public void setTodayRevenue(BigDecimal todayRevenue) { this.todayRevenue = todayRevenue; }
    public BigDecimal getPendingRevenue() { return pendingRevenue; }
    public void setPendingRevenue(BigDecimal pendingRevenue) { this.pendingRevenue = pendingRevenue; }
    public BigDecimal getAverageOrderValue() { return averageOrderValue; }
    public void setAverageOrderValue(BigDecimal averageOrderValue) { this.averageOrderValue = averageOrderValue; }
    public long getTotalUnitsSold() { return totalUnitsSold; }
    public void setTotalUnitsSold(long totalUnitsSold) { this.totalUnitsSold = totalUnitsSold; }
    public BigDecimal getPaymentSuccessRate() { return paymentSuccessRate; }
    public void setPaymentSuccessRate(BigDecimal paymentSuccessRate) { this.paymentSuccessRate = paymentSuccessRate; }
    public BigDecimal getPaymentFailureRate() { return paymentFailureRate; }
    public void setPaymentFailureRate(BigDecimal paymentFailureRate) { this.paymentFailureRate = paymentFailureRate; }
    public BigDecimal getCatalogQualityRate() { return catalogQualityRate; }
    public void setCatalogQualityRate(BigDecimal catalogQualityRate) { this.catalogQualityRate = catalogQualityRate; }
    public BigDecimal getPendingRiskShare() { return pendingRiskShare; }
    public void setPendingRiskShare(BigDecimal pendingRiskShare) { this.pendingRiskShare = pendingRiskShare; }
    public long getLowStockProductsCount() { return lowStockProductsCount; }
    public void setLowStockProductsCount(long lowStockProductsCount) { this.lowStockProductsCount = lowStockProductsCount; }
    public long getProductsMissingImage() { return productsMissingImage; }
    public void setProductsMissingImage(long productsMissingImage) { this.productsMissingImage = productsMissingImage; }
    public long getStaleProducts() { return staleProducts; }
    public void setStaleProducts(long staleProducts) { this.staleProducts = staleProducts; }
    public List<SellerRevenueTrendPoint> getRevenueTrend() { return revenueTrend; }
    public void setRevenueTrend(List<SellerRevenueTrendPoint> revenueTrend) { this.revenueTrend = revenueTrend; }
    public SellerOrdersDistribution getOrdersDistribution() { return ordersDistribution; }
    public void setOrdersDistribution(SellerOrdersDistribution ordersDistribution) { this.ordersDistribution = ordersDistribution; }
    public List<SellerCategoryDistributionItem> getCategoryDistribution() { return categoryDistribution; }
    public void setCategoryDistribution(List<SellerCategoryDistributionItem> categoryDistribution) { this.categoryDistribution = categoryDistribution; }
    public List<SellerRecentOrder> getRecentOrders() { return recentOrders; }
    public void setRecentOrders(List<SellerRecentOrder> recentOrders) { this.recentOrders = recentOrders; }
    public List<SellerTopProduct> getTopProducts() { return topProducts; }
    public void setTopProducts(List<SellerTopProduct> topProducts) { this.topProducts = topProducts; }
}
