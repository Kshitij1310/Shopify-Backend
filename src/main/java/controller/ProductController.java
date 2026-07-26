package controller;

import dto.ProductDto;
import service.ProductService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private static final Logger log = LoggerFactory.getLogger(ProductController.class);

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<List<ProductDto>> list() {
        try {
            List<ProductDto> products = productService.listAll();
            return ResponseEntity.ok(products != null ? products : List.of());
        } catch (Exception ex) {
            log.error("Failed to list products", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(List.of());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> get(@PathVariable String id) {
        try {
            return productService.get(id)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception ex) {
            log.error("Failed to fetch product id={}", id, ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/seller/{sellerId}")
    public ResponseEntity<List<ProductDto>> bySeller(@PathVariable String sellerId) {
        try {
            if (sellerId == null || sellerId.isBlank()) {
                return ResponseEntity.ok(List.of());
            }

            List<ProductDto> products = productService.listBySeller(sellerId);
            return ResponseEntity.ok(products != null ? products : List.of());
        } catch (Exception ex) {
            log.error("Failed to list products for sellerId={}", sellerId, ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(List.of());
        }
    }

    @PostMapping
    public ResponseEntity<ProductDto> create(@Valid @RequestBody ProductDto dto) {
        try {
            ProductDto created = productService.create(dto);
            return ResponseEntity.created(URI.create("/api/products/" + created.getId())).body(created);
        } catch (Exception ex) {
            log.error("Failed to create product", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/upload-image")
    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            String imagePath = productService.saveImage(file);
            return ResponseEntity.ok(java.util.Collections.singletonMap("imageUrl", imagePath));
        } catch (Exception ex) {
            log.error("Failed to upload product image", ex);
            String message = ex.getMessage() == null || ex.getMessage().isBlank() ? "Internal server error" : ex.getMessage();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", message));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> update(@PathVariable String id, @Valid @RequestBody ProductDto dto) {
        try {
            ProductDto updated = productService.update(id, dto);
            if (updated == null) return ResponseEntity.notFound().build();
            return ResponseEntity.ok(updated);
        } catch (Exception ex) {
            log.error("Failed to update product id={}", id, ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        try {
            boolean removed = productService.delete(id);
            if (!removed) return ResponseEntity.notFound().build();
            return ResponseEntity.noContent().build();
        } catch (Exception ex) {
            log.error("Failed to delete product id={}", id, ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
