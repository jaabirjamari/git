package Domain;

import java.util.Collections;
import java.util.List;

public class Beverage extends MenuItem {


    // Attributes
    private BeverageType type;  // e.g., COFFEE, TEA, SMOOTHIE

    // Constructor
    public Beverage(String name, String description, double basePrice, BeverageType type) {
        super(name, description, basePrice);
        this.type = type;
    }

    // Implement abstract methods
    @Override
    public List<Size> getAvailableSizes() {
        // Example: beverages usually have sizes, so return a predefined list or leave empty for now
        return Collections.emptyList();
    }

    @Override
    public List<Customization> getAvailableCustomizations() {
        // Example: beverages can have customizations like milk type, sugar, etc.
        return Collections.emptyList();
    }

    @Override
    public boolean supportsSize() {
        return true;
    }

    // Getter for beverage type
    public BeverageType getType() {
        return type;
    }

    // Optional: full name including type or other details
    public String getFullName() {
        return getName() + " (" + type + ")";
    }
}
