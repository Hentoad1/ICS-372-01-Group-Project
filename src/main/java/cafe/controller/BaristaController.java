package cafe.controller;

import cafe.model.*;
import cafe.service.OrderService;
import java.util.List;

public class BaristaController {
    private OrderService orderService;

    public BaristaController(OrderService orderService) {
        this.orderService = orderService;
    }

    public List<Order> getPendingOrders() {
        return orderService.getPendingOrders();
    }

    public List<Order> getFulfilledOrders() {
        return orderService.getFulfilledOrders();
    }

    public void updateOrderStatus(String orderId, OrderStatus newStatus) {
        orderService.updateOrderStatus(orderId, newStatus);
    }

    public void addOrderObserver(OrderService.OrderObserver observer) {
        orderService.addObserver(observer);
    }
}