package cafe.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Beverage extends MenuItem {
    private String size;
    private List<String> customizations;
    private Map<String, Double> customizationPrices;

    public Beverage(String id, String name, double basePrice, String size,
                    Map<String, Double> ingredientRequirements, Map<String, Double> customizationPrices) {
        super(id, name, basePrice, "Beverage", ingredientRequirements);
        this.size = size;
        this.customizations = new ArrayList<>();
        this.customizationPrices = customizationPrices;
    }

    public String getSize() { return size; }
    public void setSize(String size) { this.size = size; }
    public List<String> getCustomizations() { return customizations; }
    public void addCustomization(String customization) { customizations.add(customization); }
    public void removeCustomization(String customization) { customizations.remove(customization); }
    public Map<String, Double> getCustomizationPrices() { return customizationPrices; }

    @Override
    public double getPrice() {
        double total = basePrice;
        for (String customization : customizations) {
            total += customizationPrices.getOrDefault(customization, 0.0);
        }
        return total;
    }
}