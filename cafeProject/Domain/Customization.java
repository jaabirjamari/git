package Domain;

import java.util.HashMap;
import java.util.Map;

public class Customization {

    // Attributes
    private String name; // e.g. "Extra Shot", "Oat Milk"
    private double extraCost;
    private Map<String, Integer> additionalIngredients;

    // Constructor
    public Customization(String name, double extraCost) {
        this.name = name;
        this.extraCost = extraCost;
        this.additionalIngredients = new HashMap<>();
    }

    // Methods
    public void addIngredientRequirement(String ingredient, int quantity) {
        additionalIngredients.put(ingredient, quantity);
    }

    // Standard getters
    public String getName() {
        return name;
    }

    public double getExtraCost() {
        return extraCost;
    }

    public Map<String, Integer> getAdditionalIngredients() {
        return additionalIngredients;
    }
}
