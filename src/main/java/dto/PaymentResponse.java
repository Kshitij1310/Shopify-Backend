package dto;

public class PaymentResponse {
    private String paymentId;
    private String orderId;
    private String referenceId;
    private String status;

    public PaymentResponse() {}

    public PaymentResponse(String paymentId, String orderId, String referenceId, String status) {
        this.paymentId = paymentId;
        this.orderId = orderId;
        this.referenceId = referenceId;
        this.status = status;
    }

    public String getPaymentId() { return paymentId; }
    public void setPaymentId(String paymentId) { this.paymentId = paymentId; }
    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
    public String getReferenceId() { return referenceId; }
    public void setReferenceId(String referenceId) { this.referenceId = referenceId; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
