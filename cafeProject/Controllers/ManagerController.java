package com.cafe.Controller;

import Domain.MenuItem;
import Domain.Order;
import Domain.Ingredient;
import System.CafeSystem;
import System.InventoryManager;
import System.OrderQueue;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.util.Objects;
import java.util.List;
import java.util.Collections;
import Domain.Size;
import Domain.Customization;

/**
 * ManagerController: Acts as a Mediator for manager operations.
 * It orchestrates interactions between UI components and the core system,
 * leveraging JavaFX's observable patterns for efficient data flow.
 */
public class ManagerController {

    // Core system dependencies
    private final CafeSystem cafeSystem;
    private final InventoryManager inventoryManager;
    private final OrderQueue orderQueue;

    // Observable lists for UI binding (e.g., TableViews)
    public final ObservableList<MenuItem> menuItems;
    public final ObservableList<Ingredient> inventoryItems;
    public final ObservableList<Order> pendingOrders;

    // Observable properties for inter-component communication (e.g., selections)
    public final ObjectProperty<MenuItem> selectedMenuItem = new SimpleObjectProperty<>();
    public final ObjectProperty<Ingredient> selectedInventoryItem = new SimpleObjectProperty<>();
    public final ObjectProperty<Runnable> refreshInventoryAction = new SimpleObjectProperty<>();

    public ManagerController(CafeSystem cafeSystem) {
        this.cafeSystem = Objects.requireNonNull(cafeSystem, "CafeSystem cannot be null");
        this.inventoryManager = cafeSystem.getInventoryManager();
        this.orderQueue = cafeSystem.getOrderQueue();

        // Initialize observable lists from the system model
        this.menuItems = FXCollections.observableArrayList(cafeSystem.getAllMenuItems());
        this.inventoryItems = inventoryManager.getObservableIngredients();
        this.pendingOrders = FXCollections.observableArrayList(orderQueue.getAllPendingOrders());

        setupMediatorListeners();
    }

    // Configures listeners for mediator-specific properties
    private void setupMediatorListeners() {
        selectedMenuItem.addListener((obs, oldVal, newVal) -> System.out.println("Selected menu item: " + (newVal != null ? newVal.getName() : "none")));
        selectedInventoryItem.addListener((obs, oldVal, newVal) -> System.out.println("Selected inventory item: " + (newVal != null ? newVal.getName() : "none")));
        refreshInventoryAction.addListener((obs, oldVal, newVal) -> {
            if (newVal != null) { newVal.run(); refreshInventoryAction.set(null); }
        });
    }

    // --- Action Methods (called by View components) ---

    // Adds a new menu item to the system
    public void addMenuItem(String name, String description, double price) {
        MenuItem newItem = new MenuItem(name, description, price) { // Placeholder for concrete MenuItem
            @Override public List<Size> getAvailableSizes() { return Collections.emptyList(); }
            @Override public List<Customization> getAvailableCustomizations() { return Collections.emptyList(); }
            @Override public boolean supportsSize() { return false; }
        };
        cafeSystem.addMenuItem(newItem);
        menuItems.add(newItem); // Updates UI automatically
    }

    // Restocks a given ingredient
    public void restockIngredient(Ingredient ingredient, int amount) {
        if (ingredient != null && amount > 0) {
            ingredient.restock(amount); // Updates observable property, UI refreshes
        }
    }

    // Updates the minimum threshold for an ingredient
    public void updateIngredientMinThreshold(Ingredient ingredient, int newThreshold) {
        if (ingredient != null && newThreshold >= 0) {
            ingredient.setMinThreshold(newThreshold); // Updates observable property, UI refreshes
        }
    }

    // Attempts to fulfill a selected order
    public void fulfillSelectedOrder(Order order) {
        if (order == null || !inventoryManager.canFulfillOrder(order.getItems())) {
            System.out.println("Order cannot be fulfilled (low stock or invalid).");
            return;
        }
        inventoryManager.consumeIngredients(order.getTotalIngredients());
        order.updateStatus(Domain.OrderStatus.FULFILLED);
        orderQueue.completeOrder(order.getOrderId());
        pendingOrders.remove(order); // Updates UI automatically
    }

//     // Cleanup method (if needed for resource management)
//     public void cleanup() { /* No specific cleanup needed for this simplified version */ }
// }
