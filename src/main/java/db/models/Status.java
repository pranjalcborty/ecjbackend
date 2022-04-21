package db.models;

public enum Status {
    PENDING(0, "Pending", "yellow"),
    PROCESSING(1, "Processing", "blue"),
    FAILED(2, "Failed", "red"),
    COMPLETED(3, "Completed", "green");

    private final int statusInt;
    private final String statusStr;
    private final String statusColor;

    Status(int statusInt, String statusStr, String statusColor) {
        this.statusInt = statusInt;
        this.statusStr = statusStr;
        this.statusColor = statusColor;
    }

    public int getStatusInt() {
        return statusInt;
    }

    public String getStatusStr() {
        return statusStr;
    }

    public String getStatusColor() {
        return statusColor;
    }
}
