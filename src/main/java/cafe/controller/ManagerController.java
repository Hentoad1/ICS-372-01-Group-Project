package cafe.controller;

import cafe.model.*;
import cafe.service.*;
import java.util.List;

public class ManagerController {
    private MenuService menuService;
    private InventoryService inventoryService;
    private OrderService orderService;

    public ManagerController(MenuService menuService, InventoryService inventoryService, OrderService orderService) {
        this.menuService = menuService;
        this.inventoryService = inventoryService;
        this.orderService = orderService;
    }

    // Menu operations
    public List<MenuItem> getAllMenuItems() {
        return menuService.getAllMenuItems();
    }

    public void addMenuItem(MenuItem item) {
        menuService.addMenuItem(item);
    }

    public void removeMenuItem(String id) {
        menuService.removeMenuItem(id);
    }

    public void updateMenuItem(MenuItem item) {
        menuService.updateMenuItem(item);
    }

    // Inventory operations
    public List<InventoryItem> getAllInventory() {
        return inventoryService.getAllInventory();
    }

    public void restockItem(String itemName, double amount) {
        inventoryService.restock(itemName, amount);
    }

    public void addInventoryObserver(InventoryService.InventoryObserver observer) {
        inventoryService.addObserver(observer);
    }

    // Report operations
    public List<Order> getFulfilledOrders() {
        return orderService.getFulfilledOrders();
    }

    public double getTotalSales() {
        return orderService.getFulfilledOrders().stream()
                .mapToDouble(Order::getTotalPrice)
                .sum();
    }

    public int getTotalOrdersFulfilled() {
        return orderService.getFulfilledOrders().size();
    }
}