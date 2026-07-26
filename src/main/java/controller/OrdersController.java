package controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import model.OrderRecord;
import repository.OrderRepository;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
public class OrdersController {

    private static final Logger log = LoggerFactory.getLogger(OrdersController.class);

    private final OrderRepository repo;

    public OrdersController(OrderRepository repo) { this.repo = repo; }

    @GetMapping("/{uid}")
    public ResponseEntity<List<OrderRecord>> listByUser(@PathVariable String uid) {
        try {
            List<OrderRecord> orders = repo.findByUid(uid);
            return ResponseEntity.ok(orders != null ? orders : List.of());
        } catch (Exception ex) {
            log.error("Failed to list orders for uid={}", uid, ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(List.of());
        }
    }

    @PostMapping
    public ResponseEntity<OrderRecord> create(@RequestBody OrderRecord r) {
        try {
            if (r.getId() == null) r.setId(UUID.randomUUID());
            r.setCreatedAt(System.currentTimeMillis());
            OrderRecord saved = repo.save(r);
            return ResponseEntity.ok(saved);
        } catch (Exception ex) {
            log.error("Failed to create order record", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<OrderRecord> getById(@PathVariable UUID id) {
        try {
            return repo.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
        } catch (Exception ex) {
            log.error("Failed to fetch order by id={}", id, ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
