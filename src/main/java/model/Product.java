package model;

import jakarta.persistence.*;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @Column(columnDefinition = "uuid")
    private java.util.UUID id;

    private String name;

    private Integer quantity;

    private String category;

    @Column(length = 2000)
    private String description;

    private String imageUrl;

    private String sellerId;

    private Long createdAt;

    private Long updatedAt;

    @Column(columnDefinition = "numeric")
    private Double price;

    public java.util.UUID getId() {
        return id;
    }

    public void setId(java.util.UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getQuantity() { return quantity; }

    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public String getCategory() { return category; }

    public void setCategory(String category) { this.category = category; }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() { return imageUrl; }

    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getSellerId() { return sellerId; }

    public void setSellerId(String sellerId) { this.sellerId = sellerId; }

    public Long getCreatedAt() { return createdAt; }

    public void setCreatedAt(Long createdAt) { this.createdAt = createdAt; }

    public Long getUpdatedAt() { return updatedAt; }

    public void setUpdatedAt(Long updatedAt) { this.updatedAt = updatedAt; }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
