package com.cafe.javafxviews;

import com.cafe.Domain.*;
import com.cafe.Domain.MenuItem;
import com.cafe.System.CafeSystem;
import com.cafe.System.InventoryManager;
import com.cafe.System.OrderQueue;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.List;
import java.util.stream.Collectors;

// A central point for layout constants to ensure UI consistency.
final class LayoutConstants {
    public static final Insets PADDING_LARGE = new Insets(20);
    public static final double SPACING_DEFAULT = 10;
    private LayoutConstants() {}
}

// A simple utility to switch views within a JavaFX Stage.
public final class ViewSwitcher {
    private ViewSwitcher() {}
    public static void switchView(Stage stage, Parent view) {
        var scene = stage.getScene();
        if (scene != null) { scene.setRoot(view); }
        else { stage.setScene(new Scene(view)); }
    }
}

// The entry point for the JavaFX application.
public class CafeApp extends Application {
    private final CafeSystem cafeSystem = new CafeSystem();
    @Override
    public void start(Stage stage) {
        stage.setTitle("Brew & Bite Cafe");
        ViewSwitcher.switchView(stage, new BaristaFulfillmentView(cafeSystem).createView(stage));
        stage.show();
    }
    public static void main(String[] args) { launch(args); }
}

// A placeholder for a full CafeSystem that would manage the model.
class CafeSystem {
    private final OrderQueue orderQueue = new OrderQueue();
    private final InventoryManager inventoryManager = new InventoryManager();
    public CafeSystem() {
        // Seed some data for demonstration
        MenuItem latte = new Beverage("Latte", "Warm espresso with milk", 4.5, BeverageType.COFFEE);
        MenuItem croissant = new Pastry("Croissant", "Buttery pastry", 2.5, PastryType.CROISSANT, "Plain");
        orderQueue.addOrder(new Order(1, "Amina", List.of(new OrderItem(latte)), OrderStatus.PENDING));
        inventoryManager.addIngredient(new Ingredient("Milk", 30));
        inventoryManager.addIngredient(new Ingredient("Espresso", 50));
        inventoryManager.addIngredient(new Ingredient("Croissant Dough", 20));
        inventoryManager.getIngredient("Croissant Dough").setMinThreshold(5);
    }
    public OrderQueue getOrderQueue() { return orderQueue; }
    public InventoryManager getInventoryManager() { return inventoryManager; }
}

// The model for the order queue, using JavaFX's ObservableList.
class OrderQueue {
    private final ObservableList<Order> pendingOrders = FXCollections.observableArrayList();
    private final ObservableList<Order> completedOrders = FXCollections.observableArrayList();
    public void addOrder(Order order) { pendingOrders.add(order); }
    public void updateOrderStatus(int orderId, OrderStatus newStatus) {
        pendingOrders.stream().filter(o -> o.getOrderId() == orderId).findFirst().ifPresent(o -> {
            o.updateStatus(newStatus);
            pendingOrders.set(pendingOrders.indexOf(o), o); // Force UI update
        });
    }
    public void completeOrder(int orderId) {
        pendingOrders.stream().filter(o -> o.getOrderId() == orderId).findFirst().ifPresent(order -> {
            pendingOrders.remove(order);
            order.updateStatus(OrderStatus.FULFILLED);
            completedOrders.add(order);
        });
    }
    public ObservableList<Order> getAllPendingOrders() { return pendingOrders; }
    public ObservableList<Order> getCompletedOrders() { return completedOrders; }
}

// The model for inventory, exposing an ObservableList for low stock.
class InventoryManager {
    private final ObservableList<Ingredient> ingredients = FXCollections.observableArrayList();
    private final ObservableList<Ingredient> lowStockIngredients = FXCollections.observableArrayList();
    public void addIngredient(Ingredient ingredient) { ingredients.add(ingredient); }
    public Ingredient getIngredient(String name) {
        return ingredients.stream().filter(i -> i.getName().equals(name)).findFirst().orElse(null);
    }
    public void consume(String ingredientName, int amount) {
        Ingredient ingredient = getIngredient(ingredientName);
        if (ingredient != null && ingredient.hasEnoughQuantity(amount)) {
            ingredient.consume(amount);
            if (ingredient.isLowStock() && !lowStockIngredients.contains(ingredient)) {
                lowStockIngredients.add(ingredient);
            }
        }
    }
    public ObservableList<Ingredient> getLowStockIngredients() { return lowStockIngredients; }
}

// The BaristaFulfillmentView binds directly to the model's ObservableLists.
public final class BaristaFulfillmentView implements View {
    private final CafeSystem cafe;
    public BaristaFulfillmentView(CafeSystem cafe) { this.cafe = cafe; }
    @Override
    public Parent createView(Stage stage) {
        var root = new HBox(LayoutConstants.SPACING_DEFAULT);
        root.setPadding(LayoutConstants.PADDING_LARGE);

        var pendingBox = new VBox(LayoutConstants.SPACING_DEFAULT, new Label("Pending Orders"));
        var pendingListView = new ListView<>(cafe.getOrderQueue().getAllPendingOrders());
        pendingListView.setCellFactory(lv -> new ListCell<>() {
            @Override protected void updateItem(Order order, boolean empty) {
                super.updateItem(order, empty);
                if (empty || order == null) { setText(null); setGraphic(null); }
                else {
                    setText(String.format("Order #%d for %s - Status: %s", order.getOrderId(), order.getCustomerName(), order.getStatus()));
                    if (order.getStatus() == OrderStatus.PENDING) {
                        var startButton = new Button("Start");
                        startButton.setOnAction(e -> cafe.getOrderQueue().updateOrderStatus(order.getOrderId(), OrderStatus.IN_PROGRESS));
                        setGraphic(startButton);
                    } else { setGraphic(null); }
                }
            }
        });
        pendingBox.getChildren().add(pendingListView);
        
        var completedBox = new VBox(LayoutConstants.SPACING_DEFAULT, new Label("Completed Orders"));
        var completedListView = new ListView<>(cafe.getOrderQueue().getCompletedOrders());
        completedBox.getChildren().add(completedListView);
        
        root.getChildren().addAll(pendingBox, completedBox);
        return root;
    }
}
public interface View {
    Parent createView(Stage stage);
}
