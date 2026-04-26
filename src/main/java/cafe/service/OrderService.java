package cafe.service;

import cafe.model.*;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public class OrderService {
    private Queue<Order> pendingOrders;
    private List<Order> fulfilledOrders;
    private InventoryService inventoryService;
    private PersistenceService persistenceService;
    private List<OrderObserver> observers;

    public interface OrderObserver {
        void onOrdersUpdated();
    }

    public OrderService(PersistenceService persistenceService, InventoryService inventoryService) {
        this.pendingOrders = new ConcurrentLinkedQueue<>();
        this.fulfilledOrders = new ArrayList<>();
        this.inventoryService = inventoryService;
        this.persistenceService = persistenceService;
        this.observers = new ArrayList<>();
    }

    public void addObserver(OrderObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(OrderObserver observer) {
        observers.remove(observer);
    }

    private void notifyObservers() {
        observers.forEach(OrderObserver::onOrdersUpdated);
    }

    public boolean placeOrder(Order order) {
        for (OrderItem item : order.getItems()) {
            MenuItem menuItem = item.getMenuItem();
            Map<String, Double> requirements = menuItem.getIngredientRequirements();
            Map<String, Double> totalRequirements = new HashMap<>();
            for (Map.Entry<String, Double> entry : requirements.entrySet()) {
                totalRequirements.put(entry.getKey(), entry.getValue() * item.getQuantity());
            }
            if (!inventoryService.deductIngredients(totalRequirements)) {
                return false;
            }
        }

        pendingOrders.add(order);
        notifyObservers();
        return true;
    }

    public List<Order> getPendingOrders() {
        return new ArrayList<>(pendingOrders);
    }

    public List<Order> getFulfilledOrders() {
        return new ArrayList<>(fulfilledOrders);
    }

    public void updateOrderStatus(String orderId, OrderStatus newStatus) {
        Order foundOrder = null;

        for (Order order : pendingOrders) {
            if (order.getOrderId().equals(orderId)) {
                foundOrder = order;
                break;
            }
        }

        if (foundOrder != null) {
            foundOrder.setStatus(newStatus);
            if (newStatus == OrderStatus.FULFILLED) {
                pendingOrders.remove(foundOrder);
                fulfilledOrders.add(foundOrder);
            }
            notifyObservers();
        }
    }

    public void clear() {
        pendingOrders.clear();
        fulfilledOrders.clear();
    }
}