package controller;

import model.CheckoutAddressRecord;
import repository.CheckoutAddressRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/checkout-addresses")
public class CheckoutAddressController {

    private final CheckoutAddressRepository repo;

    public CheckoutAddressController(CheckoutAddressRepository repo) { this.repo = repo; }

    @GetMapping("/{uid}")
    public ResponseEntity<List<CheckoutAddressRecord>> list(@PathVariable String uid) {
        return ResponseEntity.ok(repo.findByUid(uid));
    }

    @PostMapping
    public ResponseEntity<CheckoutAddressRecord> create(@RequestBody CheckoutAddressRecord r) {
        if (r.getId() == null) r.setId(java.util.UUID.randomUUID().toString());
        return ResponseEntity.ok(repo.save(r));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
