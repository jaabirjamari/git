package Domain;

import java.util.Map;
import java.util.HashMap;

public class Size {

    // Attributes
    private String sizeName;           // "Small", "Medium", "Large"
    private double priceModifier;      // +$0.00, +$0.50, +$1.00
    private double ingredientMultiplier; // 1.0, 1.5, 2.0 (affects ingredient consumption)

    // Constructor
    public Size(String sizeName, double priceModifier, double ingredientMultiplier) {
        this.sizeName = sizeName;
        this.priceModifier = priceModifier;
        this.ingredientMultiplier = ingredientMultiplier;
    }

    // Methods
    public double applyPriceModifier(double basePrice) {
        return basePrice + priceModifier;
    }

    public Map<String, Integer> adjustIngredients(Map<String, Integer> baseIngredients) {
        Map<String, Integer> adjustedIngredients = new HashMap<>();
        for (Map.Entry<String, Integer> entry : baseIngredients.entrySet()) {
            int adjustedAmount = (int) Math.ceil(entry.getValue() * ingredientMultiplier);
            adjustedIngredients.put(entry.getKey(), adjustedAmount);
        }
        return adjustedIngredients;
    }

    // Standard getters
    public String getSizeName() {
        return sizeName;
    }

    public double getPriceModifier() {
        return priceModifier;
    }

    public double getIngredientMultiplier() {
        return ingredientMultiplier;
    }
}
