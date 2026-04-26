package cafe.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Order {
    private String orderId;
    private String customerName;
    private LocalDateTime timestamp;
    private List<OrderItem> items;
    private OrderStatus status;
    private double totalPrice;

    public Order(String customerName) {
        this.orderId = UUID.randomUUID().toString().substring(0, 8);
        this.customerName = customerName;
        this.timestamp = LocalDateTime.now();
        this.items = new ArrayList<>();
        this.status = OrderStatus.PENDING;
        this.totalPrice = 0.0;
    }

    public String getOrderId() { return orderId; }
    public String getCustomerName() { return customerName; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public List<OrderItem> getItems() { return items; }
    public OrderStatus getStatus() { return status; }
    public double getTotalPrice() { return totalPrice; }

    public void setStatus(OrderStatus status) { this.status = status; }

    public void addItem(OrderItem item) {
        items.add(item);
        recalculateTotal();
    }

    public void removeItem(OrderItem item) {
        items.remove(item);
        recalculateTotal();
    }

    private void recalculateTotal() {
        totalPrice = items.stream().mapToDouble(OrderItem::getTotalPrice).sum();
    }
}