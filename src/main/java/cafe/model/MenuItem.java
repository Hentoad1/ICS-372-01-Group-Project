package cafe.model;

import java.util.Map;

public abstract class MenuItem {
    protected String id;
    protected String name;
    protected double basePrice;
    protected String category;
    protected Map<String, Double> ingredientRequirements;

    public MenuItem(String id, String name, double basePrice, String category, Map<String, Double> ingredientRequirements) {
        this.id = id;
        this.name = name;
        this.basePrice = basePrice;
        this.category = category;
        this.ingredientRequirements = ingredientRequirements;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public double getBasePrice() { return basePrice; }
    public String getCategory() { return category; }
    public Map<String, Double> getIngredientRequirements() { return ingredientRequirements; }

    public void setName(String name) { this.name = name; }
    public void setBasePrice(double basePrice) { this.basePrice = basePrice; }
    public void setIngredientRequirements(Map<String, Double> requirements) { this.ingredientRequirements = requirements; }

    public abstract double getPrice();
}