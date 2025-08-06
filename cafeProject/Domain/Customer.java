package Domain;

import java.util.ArrayList;

public class Customer {

    // Attributes
    private String name;
    private Order currentOrder; // Order being built before placing

    // Constructor
    public Customer(String name) {
        this.name = name;
    }

    // Methods
    public void startNewOrder() {
        currentOrder = new Order(
            IdGenerator.generateOrderId(), // You’ll need a utility or static counter
            name,
            new ArrayList<>(),
            OrderStatus.PENDING,
            java.time.LocalDateTime.now()
        );
    }

    public void addItemToCurrentOrder(OrderItem item) {
        if (currentOrder != null) {
            currentOrder.addItem(item);
        }
    }

    public void removeItemFromCurrentOrder(OrderItem item) {
        if (currentOrder != null) {
            currentOrder.removeItem(item);
        }
    }

    public Order getCurrentOrder() {
        return currentOrder;
    }

    public void clearCurrentOrder() {
        currentOrder = null;
    }

    // Getter
    public String getName() {
        return name;
    }
}
