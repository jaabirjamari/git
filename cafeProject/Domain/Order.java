package Domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

public class Order {

    // Attributes
    private int orderId;
    private String customerName;
    private List<OrderItem> items;
    private OrderStatus status; // enum: PENDING, IN_PROGRESS, READY, FULFILLED
    private LocalDateTime orderTime;
    private double totalPrice;

    // Constructor
    public Order(int orderId, String customerName, List<OrderItem> items, OrderStatus status, LocalDateTime orderTime) {
        this.orderId = orderId;
        this.customerName = customerName;
        this.items = (items != null) ? items : new ArrayList<>();
        this.status = status;
        this.orderTime = orderTime;
        this.totalPrice = calculateTotal();
    }

    // Methods
    public void addItem(OrderItem item) {
        items.add(item);
        totalPrice = calculateTotal(); // Recalculate total
    }

    public void removeItem(OrderItem item) {
        items.remove(item);
        totalPrice = calculateTotal(); // Recalculate total
    }

    public double calculateTotal() {
        double total = 0.0;
        for (OrderItem item : items) {
            total += item.calculatePrice();
        }
        return total;
    }

    public void updateStatus(OrderStatus newStatus) {
        this.status = newStatus;
    }

    // Getters
    public int getOrderId() {
        return orderId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public LocalDateTime getOrderTime() {
        return orderTime;
    }

    public double getTotalPrice() {
        return totalPrice;
    }
}
