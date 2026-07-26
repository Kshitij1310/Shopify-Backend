package dto;

public class SellerOrdersDistribution {
    private long success;
    private long pending;
    private long failed;
    private long canceled;

    public long getSuccess() { return success; }
    public void setSuccess(long success) { this.success = success; }
    public long getPending() { return pending; }
    public void setPending(long pending) { this.pending = pending; }
    public long getFailed() { return failed; }
    public void setFailed(long failed) { this.failed = failed; }
    public long getCanceled() { return canceled; }
    public void setCanceled(long canceled) { this.canceled = canceled; }
}
