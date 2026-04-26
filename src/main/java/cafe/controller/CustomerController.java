package cafe.controller;

import cafe.model.*;
import cafe.service.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerController {
    private MenuService menuService;
    private OrderService orderService;
    private InventoryService inventoryService;
    private List<OrderItem> currentCart;

    public CustomerController(MenuService menuService, OrderService orderService, InventoryService inventoryService) {
        this.menuService = menuService;
        this.orderService = orderService;
        this.inventoryService = inventoryService;
        this.currentCart = new ArrayList<>();
    }

    public List<MenuItem> getAllMenuItems() {
        return menuService.getAllMenuItems();
    }

    public List<MenuItem> getBeverages() {
        return menuService.getItemsByCategory("Beverage");
    }

    public List<MenuItem> getPastries() {
        return menuService.getItemsByCategory("Pastry");
    }

    public void addToCart(MenuItem item, int quantity) {
        for (OrderItem cartItem : currentCart) {
            if (cartItem.getMenuItem().getId().equals(item.getId())) {
                cartItem.setQuantity(cartItem.getQuantity() + quantity);
                return;
            }
        }
        currentCart.add(new OrderItem(item, quantity));
    }

    public void removeFromCart(MenuItem item) {
        currentCart.removeIf(cartItem -> cartItem.getMenuItem().getId().equals(item.getId()));
    }

    public void updateCartQuantity(MenuItem item, int quantity) {
        for (OrderItem cartItem : currentCart) {
            if (cartItem.getMenuItem().getId().equals(item.getId())) {
                if (quantity <= 0) {
                    currentCart.remove(cartItem);
                } else {
                    cartItem.setQuantity(quantity);
                }
                return;
            }
        }
    }

    public List<OrderItem> getCurrentCart() {
        return new ArrayList<>(currentCart);
    }

    public double getCartTotal() {
        return currentCart.stream().mapToDouble(OrderItem::getTotalPrice).sum();
    }

    public boolean placeOrder(String customerName) {
        if (currentCart.isEmpty()) {
            return false;
        }

        Order order = new Order(customerName);
        for (OrderItem item : currentCart) {
            order.addItem(item);
        }

        boolean success = orderService.placeOrder(order);
        if (success) {
            currentCart.clear();
        }
        return success;
    }

    public void clearCart() {
        currentCart.clear();
    }
}