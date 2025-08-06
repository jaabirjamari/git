package Domain;

public class Ingredient {

    // Attributes
    private String name;
    private int currentQuantity;
    private String unit; // "ml", "grams", "pieces"
    private int minThreshold; // for low inventory warnings

    // Constructor
    public Ingredient(String name, int initialQuantity, String unit, int minThreshold) {
        this.name = name;
        this.currentQuantity = initialQuantity;
        this.unit = unit;
        this.minThreshold = minThreshold;
    }

    // Methods
    public boolean hasEnoughQuantity(int requiredAmount) {
        return currentQuantity >= requiredAmount;
    }

    public boolean consume(int amount) {
        if (hasEnoughQuantity(amount)) {
            currentQuantity -= amount;
            return true;
        }
        return false;
    }

    public void restock(int amount) {
        currentQuantity += amount;
    }

    public boolean isLowStock() {
        return currentQuantity <= minThreshold;
    }

    // Getters
    public String getName() {
        return name;
    }

    public int getCurrentQuantity() {
        return currentQuantity;
    }

    public String getUnit() {
        return unit;
    }

    public int getMinThreshold() {
        return minThreshold;
    }
}
