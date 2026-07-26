package migration;

import model.*;
import repository.CartRepository;
import repository.OrderRepository;
import repository.ProductRepository;
import repository.UserProfileRepository;
import repository.WishlistRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
public class MigrationRunner implements CommandLineRunner {

    @Value("${app.migration.enabled:false}")
    private boolean enabled;

    private final ObjectMapper mapper = new ObjectMapper();
    private final ProductRepository productRepository;
    private final UserProfileRepository userProfileRepository;
    private final CartRepository cartRepository;
    private final WishlistRepository wishlistRepository;
    private final OrderRepository orderRepository;

    public MigrationRunner(ProductRepository productRepository,
                           UserProfileRepository userProfileRepository,
                           CartRepository cartRepository,
                           WishlistRepository wishlistRepository,
                           OrderRepository orderRepository) {
        this.productRepository = productRepository;
        this.userProfileRepository = userProfileRepository;
        this.cartRepository = cartRepository;
        this.wishlistRepository = wishlistRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (!enabled) return;

        importProducts();
        importUsers();
        importCart();
        importWishlist();
        importOrders();
    }

    private void importProducts() {
        try (InputStream is = getResourceStream("migration/products.json")) {
            if (is == null) return;
            List<Map<String, Object>> docs = mapper.readValue(is, new TypeReference<>() {});
            for (Map<String, Object> doc : docs) {
                Product p = new Product();
                Object id = doc.get("id");
                p.setId(id != null ? UUID.fromString(String.valueOf(id)) : UUID.randomUUID());
                p.setName((String) doc.getOrDefault("name", ""));
                p.setPrice(toDouble(doc.get("price")));
                p.setQuantity((Integer) (doc.getOrDefault("quantity", 0)));
                p.setCategory((String) doc.getOrDefault("category", ""));
                p.setDescription((String) doc.getOrDefault("description", ""));
                p.setImageUrl((String) doc.getOrDefault("imageUrl", ""));
                p.setSellerId(String.valueOf(doc.getOrDefault("sellerId", "")));
                p.setCreatedAt(toLong(doc.get("createdAt")));
                p.setUpdatedAt(toLong(doc.get("updatedAt")));
                productRepository.save(p);
            }
        } catch (Exception ex) {
            System.out.println("product import failed: " + ex.getMessage());
        }
    }

    private void importUsers() {
        try (InputStream is = getResourceStream("migration/users.json")) {
            if (is == null) return;
            List<Map<String, Object>> docs = mapper.readValue(is, new TypeReference<>() {});
            for (Map<String, Object> doc : docs) {
                UserProfile u = new UserProfile();
                u.setUid(String.valueOf(doc.get("uid")));
                u.setEmail((String) doc.getOrDefault("email", ""));
                u.setRole((String) doc.getOrDefault("role", "User"));
                Object wb = doc.getOrDefault("walletBalance", 0);
                u.setWalletBalance(new BigDecimal(String.valueOf(wb)));
                u.setDisplayName((String) doc.getOrDefault("displayName", ""));
                u.setPhone((String) doc.getOrDefault("phone", ""));
                u.setPhotoURL((String) doc.getOrDefault("photoURL", ""));
                u.setPasswordHash((String) doc.getOrDefault("passwordHash", ""));
                userProfileRepository.save(u);
            }
        } catch (Exception ex) {
            System.out.println("users import failed: " + ex.getMessage());
        }
    }

    private void importCart() {
        try (InputStream is = getResourceStream("migration/cart.json")) {
            if (is == null) return;
            List<Map<String, Object>> docs = mapper.readValue(is, new TypeReference<>() {});
            for (Map<String, Object> doc : docs) {
                CartRecord c = new CartRecord();
                c.setId(String.valueOf(doc.getOrDefault("id", String.valueOf(UUID.randomUUID()))));
                c.setUid(String.valueOf(doc.getOrDefault("uid", "")));
                c.setProductId(String.valueOf(doc.getOrDefault("productId", "")));
                c.setQuantity(((Number) doc.getOrDefault("quantity", 1)).intValue());
                c.setProductJson(mapper.writeValueAsString(doc.getOrDefault("product", doc.get("productJson"))));
                cartRepository.save(c);
            }
        } catch (Exception ex) {
            System.out.println("cart import failed: " + ex.getMessage());
        }
    }

    private void importWishlist() {
        try (InputStream is = getResourceStream("migration/wishlist.json")) {
            if (is == null) return;
            List<Map<String, Object>> docs = mapper.readValue(is, new TypeReference<>() {});
            for (Map<String, Object> doc : docs) {
                WishlistRecord w = new WishlistRecord();
                w.setId(String.valueOf(doc.getOrDefault("id", String.valueOf(UUID.randomUUID()))));
                w.setUid(String.valueOf(doc.getOrDefault("uid", "")));
                w.setProductId(String.valueOf(doc.getOrDefault("productId", "")));
                w.setProductJson(mapper.writeValueAsString(doc.getOrDefault("product", doc.get("productJson"))));
                wishlistRepository.save(w);
            }
        } catch (Exception ex) {
            System.out.println("wishlist import failed: " + ex.getMessage());
        }
    }

    private void importOrders() {
        try (InputStream is = getResourceStream("migration/orders.json")) {
            if (is == null) return;
            List<Map<String, Object>> docs = mapper.readValue(is, new TypeReference<>() {});
            for (Map<String, Object> doc : docs) {
                OrderRecord o = new OrderRecord();
                Object id = doc.get("id");
                o.setId(id != null ? UUID.fromString(String.valueOf(id)) : UUID.randomUUID());
                o.setUid(String.valueOf(doc.getOrDefault("uid", "")));
                o.setItemsJson(mapper.writeValueAsString(doc.getOrDefault("items", doc.get("itemsJson"))));
                Object amt = doc.getOrDefault("amount", 0);
                o.setAmount(new BigDecimal(String.valueOf(amt)));
                o.setStatus((String) doc.getOrDefault("status", "placed"));
                o.setPaymentStatus((String) doc.getOrDefault("paymentStatus", "pending"));
                o.setPaymentMethod((String) doc.getOrDefault("paymentMethod", ""));
                o.setCouponCode((String) doc.getOrDefault("couponCode", ""));
                o.setPaymentReferenceId((String) doc.getOrDefault("paymentReferenceId", ""));
                o.setSellerIdsJson(mapper.writeValueAsString(doc.getOrDefault("sellerIds", doc.get("sellerIdsJson"))));
                o.setAddressJson(mapper.writeValueAsString(doc.getOrDefault("address", doc.get("addressJson"))));
                o.setCreatedAt(toLong(doc.get("createdAt")));
                orderRepository.save(o);
            }
        } catch (Exception ex) {
            System.out.println("orders import failed: " + ex.getMessage());
        }
    }

    private InputStream getResourceStream(String path) {
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
    }

    private Double toDouble(Object v) {
        if (v == null) return 0.0;
        if (v instanceof Number) return ((Number) v).doubleValue();
        try { return Double.parseDouble(String.valueOf(v)); } catch (Exception e) { return 0.0; }
    }

    private Long toLong(Object v) {
        if (v == null) return System.currentTimeMillis();
        if (v instanceof Number) return ((Number) v).longValue();
        try { return Long.parseLong(String.valueOf(v)); } catch (Exception e) { return System.currentTimeMillis(); }
    }
}
