package com.cafe.javafxviews;

import com.cafe.Domain.Customization;
import com.cafe.Domain.MenuItem;
import com.cafe.Domain.Order;
import com.cafe.Domain.Size;
import com.cafe.System.CafeSystem;
import com.cafe.System.Customer;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * CustomerOrderingView.
 * This class represents the user interface for customers to browse the menu and place orders.
 */
public final class CustomerOrderingView implements View {
    private final CafeSystem cafeSystem;
    private final StackPane overlayRoot = new StackPane();
    private final BorderPane mainPane = new BorderPane();
    private final Stage stage;

    private VBox customizationOverlayPanel;
    private ListView<String> orderListView;
    private Label totalLabel;

    // A simple, local-to-the-view "model" for the current order summary
    private final ObservableList<String> orderSummaryItems = FXCollections.observableArrayList();

    public CustomerOrderingView(CafeSystem cafeSystem, Stage stage) {
        this.cafeSystem = Objects.requireNonNull(cafeSystem, "CafeSystem (Controller) cannot be null");
        this.stage = stage;
    }

    @Override
    public Parent createView(Stage stage) {
        overlayRoot.getChildren().add(mainPane);
        mainPane.setPadding(new Insets(10));
        mainPane.getStyleClass().add("customer-view");

        // Top: MenuBar + TabPane for categories
        MenuBar menuBar = createMenuBar();
        TabPane tabPane = createCategoryTabs();
        VBox topBox = new VBox(menuBar, tabPane);
        mainPane.setTop(topBox);

        // Right: Order summary
        VBox orderSummary = createOrderSummaryPanel();
        mainPane.setRight(orderSummary);

        // Initial population for the first tab
        Tab initialTab = tabPane.getTabs().get(0);
        mainPane.setCenter(createMenuTilePane(initialTab.getText()));

        // Create the customization overlay panel (but keep it hidden initially)
        customizationOverlayPanel = createCustomizationOverlayPanel();
        overlayRoot.getChildren().add(customizationOverlayPanel);
        customizationOverlayPanel.setVisible(false);

        return overlayRoot;
    }

    private MenuBar createMenuBar() {
        Menu fileMenu = new Menu("File");
        MenuItem saveItem = new MenuItem("Save Orders");
        saveItem.setOnAction(e -> cafeSystem.saveOrdersRequested());
        MenuItem loadItem = new MenuItem("Load Orders");
        loadItem.setOnAction(e -> cafeSystem.loadOrdersRequested());
        fileMenu.getItems().addAll(saveItem, loadItem);
        return new MenuBar(fileMenu);
    }

    private TabPane createCategoryTabs() {
        TabPane tabPane = new TabPane();
        tabPane.setSide(Side.TOP);
        tabPane.getStyleClass().add("floating"); // Use a distinct CSS class

        Tab beverageTab = new Tab("Beverages", createMenuTilePane("Beverages"));
        beverageTab.setClosable(false);
        Tab pastryTab = new Tab("Pastries", createMenuTilePane("Pastries"));
        pastryTab.setClosable(false);

        tabPane.getTabs().addAll(beverageTab, pastryTab);

        // Listener to change center content based on selected tab
        tabPane.getSelectionModel().selectedItemProperty().addListener((obs, oldTab, newTab) -> {
            if (newTab != null) {
                mainPane.setCenter(createMenuTilePane(newTab.getText()));
            }
        });
        return tabPane;
    }

    private ScrollPane createMenuTilePane(String category) {
        TilePane tilePane = new TilePane();
        tilePane.setPadding(new Insets(15));
        tilePane.setHgap(15);
        tilePane.setVgap(15);
        tilePane.setAlignment(Pos.TOP_LEFT);

        // Filter and display menu items by category
        for (MenuItem item : cafeSystem.getMenuItemsByCategory(category)) {
            tilePane.getChildren().add(createMenuItemTile(item));
        }

        ScrollPane scrollPane = new ScrollPane(tilePane);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        return scrollPane;
    }

    private VBox createMenuItemTile(MenuItem item) {
        VBox tile = new VBox(5);
        tile.getStyleClass().add("menu-item-tile");
        tile.setAlignment(Pos.CENTER);

        ImageView imageView = new ImageView();
        try {
            // Placeholder image handling
            Image image = new Image(item.getImageUrl(), 100, 100, true, true);
            imageView.setImage(image);
        } catch (Exception e) {
            System.err.println("Failed to load image for " + item.getName());
            // Fallback for missing image
            imageView.setFitWidth(100);
            imageView.setFitHeight(100);
        }

        Label nameLabel = new Label(item.getName());
        nameLabel.getStyleClass().add("menu-item-name");
        Label priceLabel = new Label(String.format("$%.2f", item.getBasePrice()));
        priceLabel.getStyleClass().add("menu-item-price");

        tile.getChildren().addAll(imageView, nameLabel, priceLabel);

        tile.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.PRIMARY) {
                showCustomizationDialog(item);
            }
        });

        return tile;
    }

    private void showCustomizationDialog(MenuItem menuItem) {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Customize " + menuItem.getName());

        VBox content = new VBox(10);
        content.setPadding(new Insets(20));

        // Size Selection
        ComboBox<Size> sizeCombo = new ComboBox<>();
        sizeCombo.setPromptText("Select Size");
        sizeCombo.getItems().addAll(menuItem.getAvailableSizes());

        // Customization Selection
        ListView<Customization> customizationList = new ListView<>(FXCollections.observableArrayList(menuItem.getAvailableCustomizations()));
        customizationList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        content.getChildren().addAll(new Label("Size:"), sizeCombo, new Label("Customizations:"), customizationList);
        dialog.getDialogPane().setContent(content);

        ButtonType addToOrderButtonType = new ButtonType("Add to Order", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addToOrderButtonType, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addToOrderButtonType) {
                Size selectedSize = sizeCombo.getSelectionModel().getSelectedItem();
                List<Customization> selectedCustomizations = customizationList.getSelectionModel().getSelectedItems();
                cafeSystem.addToOrder(menuItem, selectedSize, selectedCustomizations);
                updateOrderSummary();
            }
            return null;
        });

        dialog.showAndWait();
    }

    private VBox createOrderSummaryPanel() {
        VBox orderBox = new VBox(10);
        orderBox.setPadding(new Insets(10));
        orderBox.setStyle("-fx-background-color: #f5f5f5; -fx-border-color: #ddd; -fx-border-width: 1; -fx-border-radius: 5; -fx-background-radius: 5;");
        orderBox.setPrefWidth(250);
        orderBox.setAlignment(Pos.TOP_CENTER);

        Label summaryTitle = new Label("Your Order");
        summaryTitle.setFont(Font.font("Arial", FontWeight.BOLD, 18));

        orderListView = new ListView<>(orderSummaryItems);
        orderListView.setPrefHeight(400);

        totalLabel = new Label("Total: $0.00");
        totalLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        
        Button placeOrderButton = new Button("Place Order");
        placeOrderButton.setPrefWidth(Double.MAX_VALUE);
        placeOrderButton.setStyle("-fx-background-color: #28a745; -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 10px; -fx-background-radius: 5;");
        placeOrderButton.setOnAction(e -> {
            cafeSystem.placeOrder();
            orderSummaryItems.clear();
            updateOrderSummary();
        });
        
        Button clearOrderButton = new Button("Clear Order");
        clearOrderButton.setPrefWidth(Double.MAX_VALUE);
        clearOrderButton.setStyle("-fx-background-color: #dc3545; -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 10px; -fx-background-radius: 5;");
        clearOrderButton.setOnAction(e -> {
            orderSummaryItems.clear();
            updateOrderSummary();
        });

        orderBox.getChildren().addAll(summaryTitle, orderListView, totalLabel, placeOrderButton, clearOrderButton);
        return orderBox;
    }

    private void updateOrderSummary() {
        // Assuming cafeSystem provides a way to get the current order items and total price
        List<String> items = cafeSystem.getCurrentOrderItems().stream()
            .map(item -> item.getQuantity() + "x " + item.getMenuItem().getName() +
                         (item.getCustomizations().isEmpty() ? "" : " + " +
                         item.getCustomizations().stream().map(Customization::getName).collect(Collectors.joining(", "))))
            .collect(Collectors.toList());
        orderSummaryItems.setAll(items);
        totalLabel.setText(String.format("Total: $%.2f", cafeSystem.getCurrentOrderTotalPrice()));
    }

    private VBox createCustomizationOverlayPanel() {
        // This is a simplified version of the customization dialog, now as an overlay panel
        // It's not used with the current dialog approach, but kept as a placeholder
        VBox panel = new VBox(20);
        panel.setAlignment(Pos.CENTER);
        panel.setPadding(new Insets(40));
        panel.setMaxSize(400, 500);
        panel.setStyle("-fx-background-color: white; -fx-border-color: #A9BA9D; -fx-border-width: 3; -fx-border-radius: 10; -fx-background-radius: 10;");

        // Placeholder for content
        Label titleLabel = new Label("Customize Item");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        Button closeButton = new Button("Close");
        closeButton.setOnAction(e -> panel.setVisible(false));

        panel.getChildren().addAll(titleLabel, new Separator(), closeButton);
        return panel;
    }
}
