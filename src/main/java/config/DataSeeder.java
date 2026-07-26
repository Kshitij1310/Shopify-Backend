package config;
import model.*;
import repository.CartRepository;
import repository.CheckoutAddressRepository;
import repository.OrderRepository;
import repository.PaymentRepository;
import repository.ProductRepository;
import repository.UserProfileRepository;
import repository.WishlistRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Configuration
@Profile({"default", "dev"})
public class DataSeeder {

    @Bean
    CommandLineRunner seedData(UserProfileRepository userProfiles,
                               ProductRepository products,
                               OrderRepository orders,
                               CartRepository carts,
                               WishlistRepository wishlists,
                               CheckoutAddressRepository addresses,
                               PaymentRepository payments) {
        return args -> {
            if (userProfiles.count() > 0 || products.count() > 0 || orders.count() > 0) {
                return;
            }

            UserProfile seller = new UserProfile();
            seller.setUid("seller_123");
            seller.setEmail("seller@example.com");
            seller.setRole("Seller");
            seller.setDisplayName("Demo Seller");
            seller.setWalletBalance(new BigDecimal("12500.00"));
            seller.setPasswordHash("$2a$10$DemoSeedPasswordHash");
            userProfiles.save(seller);

            UserProfile customer = new UserProfile();
            customer.setUid("user_123");
            customer.setEmail("customer@example.com");
            customer.setRole("User");
            customer.setDisplayName("Demo Customer");
            customer.setWalletBalance(new BigDecimal("5000.00"));
            customer.setPasswordHash("$2a$10$DemoSeedPasswordHash");
            userProfiles.save(customer);

            Product tshirt = createProduct("Classic T-Shirt", "Clothing", 799.0, 18, "seller_123", "https://images.example.com/tshirt.jpg");
            Product shoes = createProduct("Running Shoes", "Footwear", 2999.0, 4, "seller_123", "https://images.example.com/shoes.jpg");
            Product mug = createProduct("Coffee Mug", "Home", 399.0, 25, "seller_123", "https://images.example.com/mug.jpg");
            Product laptopBag = createProduct("Laptop Bag", "Accessories", 1899.0, 7, "seller_123", "https://images.example.com/bag.jpg");
            products.saveAll(List.of(tshirt, shoes, mug, laptopBag));

            long today = java.time.LocalDate.now(java.time.ZoneOffset.UTC).atStartOfDay().toInstant(java.time.ZoneOffset.UTC).toEpochMilli();
            long yesterday = today - 24L * 60L * 60L * 1000L;

            OrderRecord firstOrder = new OrderRecord();
            firstOrder.setId(UUID.randomUUID());
            firstOrder.setUid(customer.getUid());
            firstOrder.setItemsJson("[{\"productId\":\"" + tshirt.getId() + "\",\"quantity\":2}]");
            firstOrder.setAmount(new BigDecimal("1598.00"));
            firstOrder.setStatus("completed");
            firstOrder.setPaymentStatus("success");
            firstOrder.setPaymentMethod("fake-wallet");
            firstOrder.setSellerIdsJson("[\"seller_123\"]");
            firstOrder.setAddressJson("{\"line1\":\"12 Demo Street\",\"city\":\"Pune\"}");
            firstOrder.setCreatedAt(today + 4L * 60L * 60L * 1000L);

            OrderRecord secondOrder = new OrderRecord();
            secondOrder.setId(UUID.randomUUID());
            secondOrder.setUid(customer.getUid());
            secondOrder.setItemsJson("[{\"productId\":\"" + shoes.getId() + "\",\"quantity\":1}]");
            secondOrder.setAmount(new BigDecimal("2999.00"));
            secondOrder.setStatus("pending");
            secondOrder.setPaymentStatus("pending");
            secondOrder.setPaymentMethod("fake-card");
            secondOrder.setSellerIdsJson("[\"seller_123\"]");
            secondOrder.setAddressJson("{\"line1\":\"12 Demo Street\",\"city\":\"Pune\"}");
            secondOrder.setCreatedAt(yesterday + 2L * 60L * 60L * 1000L);

            orders.saveAll(List.of(firstOrder, secondOrder));

            CartRecord cart = new CartRecord();
            cart.setId(UUID.randomUUID().toString());
            cart.setUid(customer.getUid());
            cart.setProductId(mug.getId().toString());
            cart.setQuantity(2);
            cart.setProductJson("{\"id\":\"" + mug.getId() + "\",\"name\":\"Coffee Mug\"}");
            carts.save(cart);

            WishlistRecord wishlist = new WishlistRecord();
            wishlist.setId(UUID.randomUUID().toString());
            wishlist.setUid(customer.getUid());
            wishlist.setProductId(laptopBag.getId().toString());
            wishlist.setProductJson("{\"id\":\"" + laptopBag.getId() + "\",\"name\":\"Laptop Bag\"}");
            wishlists.save(wishlist);

            CheckoutAddressRecord address = new CheckoutAddressRecord();
            address.setId(UUID.randomUUID().toString());
            address.setUid(customer.getUid());
            address.setAddressJson("{\"line1\":\"12 Demo Street\",\"city\":\"Pune\",\"state\":\"MH\"}");
            address.setIsDefault(true);
            address.setCreatedAt(System.currentTimeMillis());
            addresses.save(address);

            PaymentRecord payment = new PaymentRecord();
            payment.setId(UUID.randomUUID().toString());
            payment.setOrderId(firstOrder.getId().toString());
            payment.setUid(customer.getUid());
            payment.setPaymentMethod("fake-wallet");
            payment.setStatus("success");
            payment.setReferenceId("PAY-DEMO-001");
            payment.setCreatedAt(System.currentTimeMillis());
            payments.save(payment);
        };
    }

    private Product createProduct(String name, String category, double price, int quantity, String sellerId, String imageUrl) {
        Product product = new Product();
        product.setId(UUID.randomUUID());
        product.setName(name);
        product.setCategory(category);
        product.setPrice(price);
        product.setQuantity(quantity);
        product.setSellerId(sellerId);
        product.setImageUrl(imageUrl);
        product.setDescription(name + " description");
        product.setCreatedAt(System.currentTimeMillis());
        product.setUpdatedAt(System.currentTimeMillis());
        return product;
    }
}
