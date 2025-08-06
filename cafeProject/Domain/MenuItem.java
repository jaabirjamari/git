package Domain;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

public abstract class MenuItem {

    // Attributes
    protected int itemId;
    protected String name;
    protected String description;
    protected double basePrice;
    protected Map<String, Integer> baseIngredients;

    // Constructor
    protected MenuItem(String name, String description, double basePrice) {
        this.name = name;
        this.description = description;
        this.basePrice = basePrice;
        this.baseIngredients = new HashMap<>(); // initialize to avoid null issues
    }

    // Abstract methods
    public abstract List<Size> getAvailableSizes();
    public abstract List<Customization> getAvailableCustomizations();
    public abstract boolean supportsSize();

    // Concrete method
    public Map<String, Integer> getIngredientRequirements(Size size, List<Customization> customizations) {
        Map<String, Integer> totalRequirements = new HashMap<>(baseIngredients);

        if (customizations != null) {
            for (Customization c : customizations) {
                for (Map.Entry<String, Integer> entry : c.getAdditionalIngredients().entrySet()) {
                    totalRequirements.merge(entry.getKey(), entry.getValue(), Integer::sum);
                }
            }
        }

        return totalRequirements;
    }

    // Standard getters
    public int getItemId() {
        return itemId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public Map<String, Integer> getBaseIngredients() {
        return baseIngredients;
    }
}
