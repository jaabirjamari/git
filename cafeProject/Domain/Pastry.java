package Domain;

import java.util.Collections;
import java.util.List;

public class Pastry extends MenuItem {

    // Attributes
    private Pastry type;      // enum: CROISSANT, MUFFIN, COOKIE
    private String variation;     // e.g. "Chocolate", "Blueberry"

    // Constructor
    public Pastry(String name, String description, double basePrice, Pastry type, String variation) {
        super(name, description, basePrice); // Call MenuItem constructor
        this.type = type;
        this.variation = variation;
    }

    // Implemented abstract methods
    @Override
    public List<Size> getAvailableSizes() {
        return Collections.emptyList(); // Pastries don’t support size
    }

    @Override
    public List<Customization> getAvailableCustomizations() {
        return Collections.emptyList(); // Can be customized later if needed
    }

    @Override
    public boolean supportsSize() {
        return false;
    }

    // Pastry-specific methods
    public String getFullName() {
        return variation + " " + getName(); // getName() from MenuItem
    }

    public Pastry getType() {
        return type;
    }

    public String getVariation() {
        return variation;
    }

    @Override
    public String toString() {
        return getFullName() + " - $" + getBasePrice();
    }

}
