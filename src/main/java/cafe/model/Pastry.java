package cafe.model;

import java.util.Map;

public class Pastry extends MenuItem {
    private String variation;

    public Pastry(String id, String name, double basePrice, String variation, Map<String, Double> ingredientRequirements) {
        super(id, name, basePrice, "Pastry", ingredientRequirements);
        this.variation = variation;
    }

    public String getVariation() { return variation; }
    public void setVariation(String variation) { this.variation = variation; }

    @Override
    public double getPrice() {
        return basePrice;
    }
}