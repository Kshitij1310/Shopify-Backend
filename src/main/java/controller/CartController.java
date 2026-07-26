package controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import model.CartRecord;
import repository.CartRepository;

import java.util.Map;
import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private static final Logger log = LoggerFactory.getLogger(CartController.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final CartRepository cartRepository;

    public CartController(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    @GetMapping("/{uid}")
    public ResponseEntity<List<CartRecord>> getByUser(@PathVariable String uid) {
        try {
            List<CartRecord> items = cartRepository.findByUid(uid);
            return ResponseEntity.ok(items != null ? items : List.of());
        } catch (Exception ex) {
            log.error("Failed to list cart items for uid={}", uid, ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(List.of());
        }
    }

    @PostMapping
    @Transactional
    public ResponseEntity<?> create(@RequestBody Map<String, Object> request) {
        try {
            String uid = firstNonBlank(request, "uid", "userId");
            String productId = firstNonBlank(request, "productId");
            Integer quantity = readQuantity(request.get("quantity"));

            if (uid == null || productId == null) {
                return ResponseEntity.badRequest().body(Map.of("message", "uid/userId and productId required"));
            }

            Object productPayload = request.get("product");

            CartRecord rec = new CartRecord();
            rec.setId(java.util.UUID.randomUUID().toString());
            rec.setUid(uid);
            rec.setProductId(productId);
            rec.setQuantity(quantity != null ? quantity : 1);
            rec.setProductJson(productPayload == null ? null : objectMapper.writeValueAsString(productPayload));

            CartRecord saved = cartRepository.save(rec);
            return ResponseEntity.ok(saved);
        } catch (Exception ex) {
            log.error("Failed to create cart record", ex);
            String message = ex.getMessage() == null || ex.getMessage().isBlank() ? "Internal server error" : ex.getMessage();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", message));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        try {
            cartRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception ex) {
            log.error("Failed to delete cart record id={}", id, ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private String firstNonBlank(Map<String, Object> request, String... keys) {
        for (String key : keys) {
            Object value = request.get(key);
            if (value != null) {
                String text = String.valueOf(value).trim();
                if (!text.isEmpty()) {
                    return text;
                }
            }
        }
        return null;
    }

    private Integer readQuantity(Object value) {
        if (value instanceof Number number) {
            return number.intValue();
        }

        if (value instanceof String text && !text.isBlank()) {
            try {
                return Integer.parseInt(text.trim());
            } catch (NumberFormatException ignored) {
                return null;
            }
        }

        return null;
    }
}
