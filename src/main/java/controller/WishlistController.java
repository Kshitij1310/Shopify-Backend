package controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import model.WishlistRecord;
import repository.WishlistRepository;

import java.util.Map;
import java.util.List;

@RestController
@RequestMapping("/api/wishlist")
public class WishlistController {

    private static final Logger log = LoggerFactory.getLogger(WishlistController.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final WishlistRepository repo;

    public WishlistController(WishlistRepository repo) { this.repo = repo; }

    @GetMapping("/{uid}")
    public ResponseEntity<List<WishlistRecord>> getByUser(@PathVariable String uid) {
        try {
            List<WishlistRecord> items = repo.findByUid(uid);
            return ResponseEntity.ok(items != null ? items : List.of());
        } catch (Exception ex) {
            log.error("Failed to list wishlist items for uid={}", uid, ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(List.of());
        }
    }

    @PostMapping
    @Transactional
    public ResponseEntity<?> create(@RequestBody Map<String, Object> request) {
        try {
            String uid = firstNonBlank(request, "uid", "userId");
            String productId = firstNonBlank(request, "productId");

            if (uid == null || productId == null) {
                return ResponseEntity.badRequest().body(Map.of("message", "uid/userId and productId required"));
            }

            Object productPayload = request.get("product");

            WishlistRecord record = new WishlistRecord();
            record.setId(java.util.UUID.randomUUID().toString());
            record.setUid(uid);
            record.setProductId(productId);
            record.setProductJson(productPayload == null ? null : objectMapper.writeValueAsString(productPayload));

            return ResponseEntity.ok(repo.save(record));
        } catch (Exception ex) {
            log.error("Failed to create wishlist record", ex);
            String message = ex.getMessage() == null || ex.getMessage().isBlank() ? "Internal server error" : ex.getMessage();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", message));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        try {
            repo.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception ex) {
            log.error("Failed to delete wishlist record id={}", id, ex);
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
}
