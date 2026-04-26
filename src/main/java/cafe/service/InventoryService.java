package cafe.service;

import cafe.model.InventoryItem;
import cafe.model.MenuItem;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class InventoryService {
    private Map<String, InventoryItem> inventory;
    private PersistenceService persistenceService;
    private List<InventoryObserver> observers;

    public interface InventoryObserver {
        void onInventoryUpdated();
    }

    public InventoryService(PersistenceService persistenceService) {
        this.inventory = new ConcurrentHashMap<>();
        this.persistenceService = persistenceService;
        this.observers = new ArrayList<>();
    }

    public void addObserver(InventoryObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(InventoryObserver observer) {
        observers.remove(observer);
    }

    private void notifyObservers() {
        observers.forEach(InventoryObserver::onInventoryUpdated);
    }

    public void addInventoryItem(InventoryItem item) {
        inventory.put(item.getName(), item);
        notifyObservers();
    }

    public void updateQuantity(String itemName, double quantity) {
        InventoryItem item = inventory.get(itemName);
        if (item != null) {
            item.setQuantity(quantity);
            notifyObservers();
        }
    }

    public void restock(String itemName, double amount) {
        InventoryItem item = inventory.get(itemName);
        if (item != null) {
            item.setQuantity(item.getQuantity() + amount);
            notifyObservers();
        }
    }

    public boolean deductIngredients(Map<String, Double> requirements) {
        for (Map.Entry<String, Double> req : requirements.entrySet()) {
            InventoryItem item = inventory.get(req.getKey());
            if (item == null || item.getQuantity() < req.getValue()) {
                return false;
            }
        }

        for (Map.Entry<String, Double> req : requirements.entrySet()) {
            InventoryItem item = inventory.get(req.getKey());
            item.setQuantity(item.getQuantity() - req.getValue());
        }
        notifyObservers();
        return true;
    }

    public InventoryItem getInventoryItem(String name) {
        return inventory.get(name);
    }

    public List<InventoryItem> getAllInventory() {
        return new ArrayList<>(inventory.values());
    }

    public void initializeDefaultInventory() {
        addInventoryItem(new InventoryItem("Coffee Beans", 5000, "g"));
        addInventoryItem(new InventoryItem("Milk", 10000, "ml"));
        addInventoryItem(new InventoryItem("Sugar", 5000, "g"));
        addInventoryItem(new InventoryItem("Flour", 8000, "g"));
        addInventoryItem(new InventoryItem("Butter", 3000, "g"));
        addInventoryItem(new InventoryItem("Chocolate Chips", 2000, "g"));
        addInventoryItem(new InventoryItem("Tea Leaves", 2000, "g"));
        addInventoryItem(new InventoryItem("Blueberries", 1500, "g"));
    }

    public void clear() {
        inventory.clear();
    }
}