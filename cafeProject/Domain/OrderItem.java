package Domain;

import java.util.List;
import java.util.Map;

import Observer.PricingStrategy;
import Observer.StandardPricingStrategy;

import java.util.HashMap;

public class OrderItem {

    // Attributes
    private MenuItem menuItem;
    private Size size; // null for pastries
    private List<Customization> customizations;
    private int quantity;
    private double itemPrice;
    private PricingStrategy pricingStrategy = new StandardPricingStrategy();

    // Constructor
    public OrderItem(MenuItem item, Size size, List<Customization> customizations, int quantity) {
        this.menuItem = item;
        this.size = size;
        this.customizations = customizations;
        this.quantity = quantity;
        this.itemPrice = calculatePrice(); // Calculate once during construction
    }

    // Methods
    public double calculatePrice() {
        return pricingStrategy.calculatePrice(menuItem, size, customizations) * quantity;
    }

    public Map<String, Integer> getIngredientRequirements() {
        // Assuming MenuItem has a method to get base ingredients and their amounts
        Map<String, Integer> requirements = new HashMap<>();
        if (menuItem != null) {
            requirements.putAll(menuItem.getBaseIngredients());

            // Add logic here if customizations affect ingredients
            for (Customization c : customizations) {
                Map<String, Integer> customIngredients = c.getAdditionalIngredients();
                for (String ingredient : customIngredients.keySet()) {
                    requirements.put(ingredient,
                        requirements.getOrDefault(ingredient, 0) + customIngredients.get(ingredient));
                }
            }
        }
        return requirements;
    }

    // Getters
    public MenuItem getMenuItem() {
        return menuItem;
    }

    public Size getSize() {
        return size;
    }

    public List<Customization> getCustomizations() {
        return customizations;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getItemPrice() {
        return itemPrice;
    }

    @Override
    public String toString() {
        return quantity + " x " + menuItem.getName() + " (" + size + ") - $" + itemPrice;
    }

}
