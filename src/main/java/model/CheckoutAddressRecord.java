package model;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "checkout_address_records")
public class CheckoutAddressRecord {
    @Id
    private String id;

    private String uid;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private String addressJson;

    private Boolean isDefault;
    private Long createdAt;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getUid() { return uid; }
    public void setUid(String uid) { this.uid = uid; }
    public String getAddressJson() { return addressJson; }
    public void setAddressJson(String addressJson) { this.addressJson = addressJson; }
    public Boolean getIsDefault() { return isDefault; }
    public void setIsDefault(Boolean isDefault) { this.isDefault = isDefault; }
    public Long getCreatedAt() { return createdAt; }
    public void setCreatedAt(Long createdAt) { this.createdAt = createdAt; }
}
