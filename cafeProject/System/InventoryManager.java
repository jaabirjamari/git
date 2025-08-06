package System;

import Domain.Ingredient;
import Domain.OrderItem;
import java.util.*;
import java.util.stream.Collectors;
import Observer.InventoryObserver;
import Observer.Observer;
import Observer.Subject;


public class InventoryManager implements Subject {

    // Attributes
    private Map<String, Ingredient> ingredients;
    private List<InventoryObserver> observers;

    // Constructor
    public InventoryManager() {
        ingredients = new HashMap<>();
        observers = new ArrayList<>();
    }

    // Inventory management methods
    public void addIngredient(Ingredient ingredient) {
        ingredients.put(ingredient.getName(), ingredient);
    }

    public Ingredient getIngredient(String name) {
        return ingredients.get(name);
    }

    public Map<String, Integer> getCurrentLevels() {
        Map<String, Integer> levels = new HashMap<>();
        for (Ingredient ingredient : ingredients.values()) {
            levels.put(ingredient.getName(), ingredient.getCurrentQuantity());
        }
        return levels;
    }

    public boolean hasEnoughIngredients(Map<String, Integer> required) {
        for (Map.Entry<String, Integer> entry : required.entrySet()) {
            Ingredient ingredient = ingredients.get(entry.getKey());
            if (ingredient == null || !ingredient.hasEnoughQuantity(entry.getValue())) {
                return false;
            }
        }
        return true;
    }

    public boolean consumeIngredients(Map<String, Integer> toConsume) {
        if (!hasEnoughIngredients(toConsume)) {
            return false;
        }

        for (Map.Entry<String, Integer> entry : toConsume.entrySet()) {
            Ingredient ingredient = ingredients.get(entry.getKey());
            ingredient.consume(entry.getValue());
            notifyObservers("INVENTORY_CHANGED", ingredient);
            if (ingredient.isLowStock()) {
                notifyObservers("LOW_STOCK", ingredient);
            }
        }

        return true;
    }

    public void restockIngredient(String ingredientName, int quantity) {
        Ingredient ingredient = ingredients.get(ingredientName);
        if (ingredient != null) {
            ingredient.restock(quantity);
            notifyObservers("INVENTORY_CHANGED", ingredient);
        }
    }

    public boolean canFulfillOrder(List<OrderItem> items) {
        Map<String, Integer> totalRequired = new HashMap<>();
        for (OrderItem item : items) {
            Map<String, Integer> itemRequirements = item.getIngredientRequirements();
            for (Map.Entry<String, Integer> entry : itemRequirements.entrySet()) {
                totalRequired.merge(entry.getKey(), entry.getValue(), Integer::sum);
            }
        }
        return hasEnoughIngredients(totalRequired);
    }

    public List<String> getLowStockIngredients() {
        return ingredients.values().stream()
                .filter(Ingredient::isLowStock)
                .map(Ingredient::getName)
                .collect(Collectors.toList());
    }

    // Observer pattern methods
    public void addObserver(Observer observer) {
        if (observer instanceof InventoryObserver) {
            observers.add((InventoryObserver) observer);
        }
    }

    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    public void notifyObservers(String changeType, Object data) {
        Ingredient ingredient = (Ingredient) data;
        for (InventoryObserver observer : observers) {
            switch (changeType) {
                case "INVENTORY_CHANGED":
                    observer.onInventoryChanged(ingredient.getName(), ingredient.getCurrentQuantity());
                    break;
                case "LOW_STOCK":
                    observer.onLowStock(ingredient.getName(), ingredient.getCurrentQuantity());
                    break;
            }
        }
    }
}  
