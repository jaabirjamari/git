import com.cafe.Domain.MenuItem;
import com.cafe.Domain.Order;
import com.cafe.Domain.OrderStatus;
import com.cafe.Domain.Ingredient;
import com.cafe.System.CafeSystem;
import com.cafe.System.InventoryObserver;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;
import java.util.Objects;

final class LayoutConstants {
    public static final double SPACING_DEFAULT = 10;
}

// --- MAIN MANAGER VIEW CLASS ---
public final class ManagerOperationsView implements View, InventoryObserver {
    private final CafeSystem cafe;
    private final ObservableList<Ingredient> inventory = FXCollections.observableArrayList();
    private final TabPane tabs;
    private final Label statusLabel;

    public ManagerOperationsView(CafeSystem cafe) {
        this.cafe = Objects.requireNonNull(cafe);
        this.tabs = createOperationsTabs(cafe, inventory);
        this.statusLabel = new Label();
        this.statusLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: red;");
    }

    @Override
    public Parent createView(Stage stage) {
        var root = new VBox(LayoutConstants.SPACING_DEFAULT, tabs, statusLabel);
        root.setPadding(new Insets(10));

        // The View registers itself as an observer of the model
        cafe.addInventoryObserver(this);

        // Populate the inventory list from the model
        inventory.setAll(cafe.getInventory());

        return root;
    }

    public void cleanup() {
        cafe.removeInventoryObserver(this);
    }
    
    // InventoryObserver implementation
    @Override
    public void onInventoryChanged(String name, int level) {
        // Find and update the specific item in the view's list
        runFx(() -> {
            inventory.stream()
                .filter(item -> name.equals(item.getName()))
                .findFirst()
                .ifPresent(item -> item.setQuantity(level)); // Assuming setQuantity exists
        });
    }

    @Override
    public void onLowStock(String name, int level) {
        runFx(() -> statusLabel.setText("Low stock: " + name + " (" + level + ")"));
    }

    // --- VIEW UTILITY METHOD ---
    private TabPane createOperationsTabs(CafeSystem cafe, ObservableList<Ingredient> inventory) {
        var tabs = new TabPane();
        tabs.getTabs().setAll(
            createMenuTab(cafe),
            createInventoryTab(inventory)
        );
        return tabs;
    }

    private Tab createMenuTab(CafeSystem cafe) {
        var menuTab = new Tab("Menu");
        var menuTable = new TableView<MenuItem>(
            FXCollections.observableArrayList(cafe.getAllMenuItems())
        );

        // Define columns for the menu table
        var nameCol = new TableColumn<MenuItem, String>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        var priceCol = new TableColumn<MenuItem, Double>("Price");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        menuTable.getColumns().add(nameCol);
        menuTable.getColumns().add(priceCol);
        
        menuTab.setContent(menuTable);
        return menuTab;
    }
    
    private Tab createInventoryTab(ObservableList<Ingredient> inventory) {
        var invTab = new Tab("Inventory");
        var invTable = new TableView<>(inventory);
        
        // Define columns for the inventory table
        var nameCol = new TableColumn<Ingredient, String>("Ingredient");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        
        var quantityCol = new TableColumn<Ingredient, Number>("Quantity");
        quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        
        var unitCol = new TableColumn<Ingredient, String>("Unit");
        unitCol.setCellValueFactory(new PropertyValueFactory<>("unit"));

        invTable.getColumns().add(nameCol);
        invTable.getColumns().add(quantityCol);
        invTable.getColumns().add(unitCol);
        
        invTab.setContent(invTable);
        return invTab;
    }

    public static void runFx(Runnable action) {
        if (Platform.isFxApplicationThread()) action.run();
        else Platform.runLater(action);
    }
}
