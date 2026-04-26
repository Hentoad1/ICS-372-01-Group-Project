package cafe.model;

public class InventoryItem {
    private String name;
    private double quantity;
    private String unit;

    public InventoryItem(String name, double quantity, String unit) {
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
    }

    public String getName() { return name; }
    public double getQuantity() { return quantity; }
    public String getUnit() { return unit; }
    public void setQuantity(double quantity) { this.quantity = quantity; }
}