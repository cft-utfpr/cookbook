package cookbook.recipe;

public class Ingredient {
    private String name;
    private boolean available;
    private String unit;
    private int quantity;

    public String getName() { return name; }
    public boolean isAvailable() { return available; }
    public String getUnit() { return unit; }
    public int getQuantity() { return quantity; }
}
