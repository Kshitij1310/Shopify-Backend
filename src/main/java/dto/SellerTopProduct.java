package dto;

import java.math.BigDecimal;

public class SellerTopProduct {
    private String id;
    private String name;
    private BigDecimal revenue = BigDecimal.ZERO;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public BigDecimal getRevenue() { return revenue; }
    public void setRevenue(BigDecimal revenue) { this.revenue = revenue; }
}
