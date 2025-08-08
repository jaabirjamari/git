import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class BaristaFulfillmentView implements View {
    private final BaristaController controller;

    private TableView<Order> ordersTable;
    private TextArea orderDetailsArea;
    private ComboBox<OrderStatus> statusSelector;
    private Label statusLabel;

    private static final Insets PADDING_SMALL = new Insets(10);
    private static final double SPACING_SMALL = 10;

    public BaristaFulfillmentView(BaristaController controller) {
        this.controller = controller;
    }

    @Override
    public Parent createView(Stage stage) {
        BorderPane root = new BorderPane();
        root.setPadding(PADDING_SMALL);

        VBox topBox = createTopSection();
        root.setTop(topBox);

        ordersTable = createOrdersTable(controller.getPendingOrdersObs());
        root.setCenter(ordersTable);

        VBox rightBox = createRightSection();
        root.setRight(rightBox);

        HBox bottomBox = createBottomSection();
        root.setBottom(bottomBox);

        addEventListeners();
        return root;
    }

    private VBox createTopSection() {
        Label dashboardTitle = new Label("Barista Dashboard");
        dashboardTitle.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        Label currentUserLabel = new Label("Logged in as: Barista User");
        VBox topBox = new VBox(SPACING_SMALL, dashboardTitle, currentUserLabel);
        topBox.setAlignment(Pos.CENTER);
        return topBox;
    }

    private TableView<Order> createOrdersTable(ObservableList<Order> orders) {
        TableView<Order> table = new TableView<>(orders);
        table.setPlaceholder(new Label("No pending orders."));

        TableColumn<Order, Integer> orderIdColumn = new TableColumn<>("Order ID");
        orderIdColumn.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        orderIdColumn.setPrefWidth(80);

        TableColumn<Order, String> customerNameColumn = new TableColumn<>("Customer");
        customerNameColumn.setCellValueFactory(cellData ->
            new ReadOnlyStringWrapper(cellData.getValue().getCustomer().getName()));
        customerNameColumn.setPrefWidth(120);

        TableColumn<Order, List<OrderItem>> itemsColumn = new TableColumn<>("Items");
        itemsColumn.setCellValueFactory(new PropertyValueFactory<>("items"));
        itemsColumn.setPrefWidth(200);
        itemsColumn.setCellFactory(tc -> new TableCell<>() {
            @Override
            protected void updateItem(List<OrderItem> items, boolean empty) {
                super.updateItem(items, empty);
                setText(empty || items == null ? null :
                        items.stream()
                             .map(item -> item.getQuantity() + "x " + item.getMenuItem().getName())
                             .collect(Collectors.joining(", ")));
            }
        });

        TableColumn<Order, OrderStatus> statusColumn = new TableColumn<>("Status");
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusColumn.setPrefWidth(100);

        TableColumn<Order, String> timeColumn = new TableColumn<>("Time");
        timeColumn.setCellValueFactory(cellData ->
            new ReadOnlyStringWrapper(cellData.getValue().getOrderTime()
                .format(DateTimeFormatter.ofPattern("HH:mm:ss"))));
        timeColumn.setPrefWidth(100);

        TableColumn<Order, Double> totalColumn = new TableColumn<>("Total");
        totalColumn.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
        totalColumn.setPrefWidth(80);
        totalColumn.setCellFactory(tc -> new TableCell<>() {
            @Override
            protected void updateItem(Double price, boolean empty) {
                super.updateItem(price, empty);
                setText(empty || price == null ? null : String.format("$%.2f", price));
            }
        });

        table.getColumns().addAll(orderIdColumn, customerNameColumn, itemsColumn,
                                  statusColumn, timeColumn, totalColumn);
        return table;
    }

    private VBox createRightSection() {
        VBox rightBox = new VBox(SPACING_SMALL);
        rightBox.setPadding(PADDING_SMALL);
        rightBox.setAlignment(Pos.TOP_CENTER);

        Label detailsLabel = new Label("Selected Order Details:");
        detailsLabel.setStyle("-fx-font-weight: bold;");

        orderDetailsArea = new TextArea("Select an order from the table to see details.");
        orderDetailsArea.setEditable(false);
        orderDetailsArea.setWrapText(true);
        orderDetailsArea.setPrefRowCount(10);

        statusSelector = new ComboBox<>();
        statusSelector.getItems().addAll(OrderStatus.PENDING, OrderStatus.IN_PROGRESS, OrderStatus.READY);

        Button updateStatusButton = new Button("Update Status");
        updateStatusButton.setOnAction(event -> {
            Order selected = ordersTable.getSelectionModel().getSelectedItem();
            if (selected != null && statusSelector.getValue() != null) {
                controller.updateOrderStatus(selected, statusSelector.getValue());
            }
        });

        Button completeOrderButton = new Button("Complete Order");
        completeOrderButton.setOnAction(event -> {
            Order selected = ordersTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                controller.completeOrder(selected);
            }
        });

        rightBox.getChildren().addAll(detailsLabel, orderDetailsArea,
                                      statusSelector, updateStatusButton, completeOrderButton);
        return rightBox;
    }

    private HBox createBottomSection() {
        statusLabel = new Label("Ready.");
        statusLabel.setStyle("-fx-font-weight: bold;");
        HBox bottomBox = new HBox(statusLabel);
        bottomBox.setAlignment(Pos.CENTER_LEFT);
        bottomBox.setPadding(PADDING_SMALL);
        return bottomBox;
    }

    private void addEventListeners() {
        ordersTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                displayOrderDetails(newSel);
                statusSelector.setValue(newSel.getStatus());
            } else {
                orderDetailsArea.setText("Select an order from the table to see details.");
                statusSelector.setValue(null);
            }
        });
    }

    private void displayOrderDetails(Order order) {
        StringBuilder details = new StringBuilder();
        details.append("Order #").append(order.getOrderId()).append("\n")
               .append("Customer: ").append(order.getCustomer().getName()).append("\n")
               .append("Status: ").append(order.getStatus()).append("\n\n")
               .append("Items:\n");
        order.getItems().forEach(item ->
            details.append("- ").append(item.getMenuItem().getName())
                   .append(" (x").append(item.getQuantity()).append(")\n"));
        details.append("\nTotal Price: $").append(String.format("%.2f", order.getTotalPrice()));
        orderDetailsArea.setText(details.toString());
    }
}
