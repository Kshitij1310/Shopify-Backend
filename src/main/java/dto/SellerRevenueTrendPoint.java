package dto;

import java.math.BigDecimal;

public class SellerRevenueTrendPoint {
    private long dayStartUtcMs;
    private BigDecimal revenue = BigDecimal.ZERO;

    public long getDayStartUtcMs() { return dayStartUtcMs; }
    public void setDayStartUtcMs(long dayStartUtcMs) { this.dayStartUtcMs = dayStartUtcMs; }
    public BigDecimal getRevenue() { return revenue; }
    public void setRevenue(BigDecimal revenue) { this.revenue = revenue; }
}
