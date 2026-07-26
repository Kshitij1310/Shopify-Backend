package model;

import jakarta.persistence.*;

@Entity
@Table(name = "payment_records")
public class PaymentRecord {
    @Id
    private String id;

    private String orderId;
    private String uid;
    private String paymentMethod;
    private String status;
    private String referenceId;
    private Long createdAt;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
    public String getUid() { return uid; }
    public void setUid(String uid) { this.uid = uid; }
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getReferenceId() { return referenceId; }
    public void setReferenceId(String referenceId) { this.referenceId = referenceId; }
    public Long getCreatedAt() { return createdAt; }
    public void setCreatedAt(Long createdAt) { this.createdAt = createdAt; }
}
