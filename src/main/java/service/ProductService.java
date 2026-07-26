package service;

import dto.ProductDto;
import model.Product;
import repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<ProductDto> listAll() {
        return productRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    public List<ProductDto> listBySeller(String sellerId) {
        return productRepository.findAll().stream()
                .filter(p -> sellerId.equals(p.getSellerId()))
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public Optional<ProductDto> get(String id) {
        try {
            java.util.UUID uuid = java.util.UUID.fromString(id);
            return productRepository.findById(uuid).map(this::toDto);
        } catch (Exception ex) {
            return Optional.empty();
        }
    }

    public ProductDto create(ProductDto dto) {
        Product p = new Product();
        if (dto.getId() != null) {
            try { p.setId(java.util.UUID.fromString(dto.getId())); } catch (Exception e) { /* ignore */ }
        }
        if (p.getId() == null) p.setId(java.util.UUID.randomUUID());
        p.setName(dto.getName());
        p.setQuantity(dto.getQuantity() != null ? dto.getQuantity() : 0);
        p.setCategory(dto.getCategory() != null ? dto.getCategory() : "");
        p.setDescription(dto.getDescription() != null ? dto.getDescription() : "");
        p.setImageUrl(dto.getImageUrl() != null ? dto.getImageUrl() : "");
        p.setSellerId(dto.getSellerId() != null ? dto.getSellerId() : "");
        p.setPrice(dto.getPrice());
        p.setCreatedAt(System.currentTimeMillis());
        p.setUpdatedAt(System.currentTimeMillis());
        Product saved = productRepository.save(p);
        return toDto(saved);
    }

    public ProductDto update(String id, ProductDto dto) {
        try {
            java.util.UUID uuid = java.util.UUID.fromString(id);
            Optional<Product> opt = productRepository.findById(uuid);
            if (opt.isEmpty()) return null;
            Product p = opt.get();
            p.setName(dto.getName());
            if (dto.getQuantity() != null) p.setQuantity(dto.getQuantity());
            p.setCategory(dto.getCategory());
            p.setDescription(dto.getDescription());
            p.setImageUrl(dto.getImageUrl());
            p.setSellerId(dto.getSellerId());
            p.setPrice(dto.getPrice());
            p.setUpdatedAt(System.currentTimeMillis());
            productRepository.save(p);
            return toDto(p);
        } catch (Exception ex) {
            return null;
        }
    }

    public boolean delete(String id) {
        try {
            java.util.UUID uuid = java.util.UUID.fromString(id);
            if (!productRepository.existsById(uuid)) return false;
            productRepository.deleteById(uuid);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public String saveImage(org.springframework.web.multipart.MultipartFile file) throws Exception {
        if (file == null || file.isEmpty()) throw new IllegalArgumentException("File is required");
        java.nio.file.Path uploads = java.nio.file.Paths.get("uploads", "products");
        java.nio.file.Files.createDirectories(uploads);
        String filename = java.util.UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        java.nio.file.Path dest = uploads.resolve(filename);
        try (java.io.InputStream in = file.getInputStream()) {
            java.nio.file.Files.copy(in, dest);
        }
        return "/uploads/products/" + filename;
    }

    private ProductDto toDto(Product p) {
        ProductDto d = new ProductDto();
        d.setId(p.getId() != null ? p.getId().toString() : null);
        d.setName(p.getName());
        d.setQuantity(p.getQuantity());
        d.setCategory(p.getCategory());
        d.setDescription(p.getDescription());
        d.setImageUrl(p.getImageUrl());
        d.setSellerId(p.getSellerId());
        d.setPrice(p.getPrice());
        return d;
    }
}
