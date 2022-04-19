package db.models;

public enum Status {
    PENDING(0, "Pending"),
    PROCESSING(1, "Processing"),
    FAILED(2, "Failed"),
    COMPLETED(3, "Completed");

    private final int statusInt;
    private final String statusStr;

    Status(int statusInt, String statusStr) {
        this.statusInt = statusInt;
        this.statusStr = statusStr;
    }

    public int getStatusInt() {
        return statusInt;
    }

    public String getStatusStr() {
        return statusStr;
    }
}
