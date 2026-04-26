package cafe.model;

public enum OrderStatus {
    PENDING("Pending"),
    IN_PROGRESS("In Progress"),
    READY_FOR_PICKUP("Ready for Pickup"),
    FULFILLED("Fulfilled");

    private String displayName;

    OrderStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}