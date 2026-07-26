package dto;

import java.math.BigDecimal;

public class SellerRecentOrder {
    private String id;
    private long createdAt;
    private BigDecimal amount = BigDecimal.ZERO;
    private String status;
    private String paymentStatus;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }
}
