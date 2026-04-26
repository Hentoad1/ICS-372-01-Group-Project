package cafe.service;

import cafe.factory.MenuItemFactory;
import cafe.model.MenuItem;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class MenuService {
    private Map<String, MenuItem> menuItems;
    private PersistenceService persistenceService;

    public MenuService(PersistenceService persistenceService) {
        this.menuItems = new ConcurrentHashMap<>();
        this.persistenceService = persistenceService;
    }

    public void addMenuItem(MenuItem item) {
        menuItems.put(item.getId(), item);
    }

    public void removeMenuItem(String id) {
        menuItems.remove(id);
    }

    public void updateMenuItem(MenuItem item) {
        menuItems.put(item.getId(), item);
    }

    public MenuItem getMenuItem(String id) {
        return menuItems.get(id);
    }

    public List<MenuItem> getAllMenuItems() {
        return new ArrayList<>(menuItems.values());
    }

    public List<MenuItem> getItemsByCategory(String category) {
        return menuItems.values().stream()
                .filter(item -> item.getCategory().equals(category))
                .toList();
    }

    public void initializeDefaultMenu() {
        Map<String, Double> customizationPrices = MenuItemFactory.getDefaultCustomizationPrices();
        Map<String, Double> sizeMultipliers = MenuItemFactory.getSizeMultipliers();

        Map<String, Double> latteIngredients = new HashMap<>();
        latteIngredients.put("Coffee Beans", 18.0);
        latteIngredients.put("Milk", 200.0);
        latteIngredients.put("Sugar", 10.0);

        for (Map.Entry<String, Double> sizeEntry : sizeMultipliers.entrySet()) {
            String size = sizeEntry.getKey();
            double multiplier = sizeEntry.getValue();
            double basePrice = 3.50 * multiplier;
            String id = "latte_" + size.toLowerCase();
            Beverage latte = (Beverage) MenuItemFactory.createBeverage(id, "Latte", basePrice, size,
                    new HashMap<>(latteIngredients), customizationPrices);
            addMenuItem(latte);
        }

        Map<String, Double> teaIngredients = new HashMap<>();
        teaIngredients.put("Tea Leaves", 5.0);
        teaIngredients.put("Sugar", 5.0);

        Beverage greenTea = (Beverage) MenuItemFactory.createBeverage("green_tea", "Green Tea", 2.50, "Regular",
                new HashMap<>(teaIngredients), customizationPrices);
        addMenuItem(greenTea);

        Map<String, Double> croissantIngredients = new HashMap<>();
        croissantIngredients.put("Flour", 50.0);
        croissantIngredients.put("Butter", 25.0);
        croissantIngredients.put("Sugar", 5.0);

        Pastry butterCroissant = (Pastry) MenuItemFactory.createPastry("croissant_butter", "Butter Croissant", 3.00, "Butter",
                new HashMap<>(croissantIngredients));
        addMenuItem(butterCroissant);

        Map<String, Double> muffinIngredients = new HashMap<>();
        muffinIngredients.put("Flour", 60.0);
        muffinIngredients.put("Butter", 20.0);
        muffinIngredients.put("Sugar", 15.0);
        muffinIngredients.put("Blueberries", 30.0);

        Pastry blueberryMuffin = (Pastry) MenuItemFactory.createPastry("muffin_blueberry", "Blueberry Muffin", 3.50, "Blueberry",
                new HashMap<>(muffinIngredients));
        addMenuItem(blueberryMuffin);

        Map<String, Double> cookieIngredients = new HashMap<>();
        cookieIngredients.put("Flour", 30.0);
        cookieIngredients.put("Butter", 15.0);
        cookieIngredients.put("Sugar", 10.0);
        cookieIngredients.put("Chocolate Chips", 20.0);

        Pastry chocChipCookie = (Pastry) MenuItemFactory.createPastry("cookie_choc", "Chocolate Chip Cookie", 2.50, "Chocolate Chip",
                new HashMap<>(cookieIngredients));
        addMenuItem(chocChipCookie);
    }

    public void clear() {
        menuItems.clear();
    }
}