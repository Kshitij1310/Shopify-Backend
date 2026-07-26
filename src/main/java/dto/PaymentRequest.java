package dto;

import jakarta.validation.constraints.NotBlank;

public class PaymentRequest {
    @NotBlank
    private String orderId;

    @NotBlank
    private String uid;

    @NotBlank
    private String paymentMethod;

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
    public String getUid() { return uid; }
    public void setUid(String uid) { this.uid = uid; }
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
}
