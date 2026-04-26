package cafe.factory;

import cafe.model.*;
import java.util.HashMap;
import java.util.Map;

public class MenuItemFactory {

    public static MenuItem createBeverage(String id, String name, double basePrice, String size,
                                          Map<String, Double> ingredientRequirements,
                                          Map<String, Double> customizationPrices) {
        return new Beverage(id, name, basePrice, size, ingredientRequirements, customizationPrices);
    }

    public static MenuItem createPastry(String id, String name, double basePrice, String variation,
                                        Map<String, Double> ingredientRequirements) {
        return new Pastry(id, name, basePrice, variation, ingredientRequirements);
    }

    public static Map<String, Double> getDefaultCustomizationPrices() {
        Map<String, Double> prices = new HashMap<>();
        prices.put("Extra Shot", 0.75);
        prices.put("Decaf", 0.50);
        prices.put("Oat Milk", 1.00);
        prices.put("Almond Milk", 1.00);
        prices.put("Sugar-Free Syrup", 0.50);
        prices.put("Whipped Cream", 0.50);
        prices.put("Vanilla Syrup", 0.50);
        prices.put("Caramel Syrup", 0.50);
        return prices;
    }

    public static Map<String, Double> getSizeMultipliers() {
        Map<String, Double> multipliers = new HashMap<>();
        multipliers.put("Small", 1.0);
        multipliers.put("Medium", 1.3);
        multipliers.put("Large", 1.6);
        return multipliers;
    }
}