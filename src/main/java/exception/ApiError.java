package exception;

import java.time.Instant;

public class ApiError {
    private final Instant timestamp = Instant.now();
    private final int status;
    private final String message;

    public ApiError(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public Instant getTimestamp() { return timestamp; }
    public int getStatus() { return status; }
    public String getMessage() { return message; }
}
