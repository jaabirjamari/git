

import java.util.ArrayList;

public class Barista extends User {
    private String userName;        
    private String password;        

    // Orders that the barista will manage
    private ArrayList<Orders> allOrders = new ArrayList<>();

    public Barista(String name, String userName, String password) {
        super(name);
        this.userName = userName;
        this.password = password;
    }

    @Override
    public String getRole() {
        return "Barista";
    }

    // Login method
    public boolean login(String inputUsername, String inputPassword) {
        return this.userName.equals(inputUsername) && this.password.equals(inputPassword);
    }

    // Assign orders to this barista
    public void setOrders(ArrayList<Orders> orders) {
        this.allOrders = orders;
    }

    // See all placed orders
    public String seePlaceOrder() {
        if (allOrders.isEmpty()) {
            return "No orders placed yet.";
        }

        StringBuilder sb = new StringBuilder("Current Orders:\n");
        for (int i = 0; i < allOrders.size(); i++) {
            sb.append("Order ").append(i + 1).append(": ").append(allOrders.get(i)).append("\n");
        }
        return sb.toString();
    }

    // Update order status (unfinished → finished)
    public String updateOrder(int orderIndex) {
        if (orderIndex >= 0 && orderIndex < allOrders.size()) {
            Orders order = allOrders.get(orderIndex);
            order.setFinished(true);
            return "Order " + (orderIndex + 1) + " marked as finished.";
        } else {
            return "Invalid order index.";
        }
    }

    // Notify that order is ready for pickup
    public String readyForPickup(int orderIndex) {
        if (orderIndex >= 0 && orderIndex < allOrders.size()) {
            Orders order = allOrders.get(orderIndex);
            if (order.isFinished()) {
                return "Order " + (orderIndex + 1) + " is ready for pickup!";
            } else {
                return "Order " + (orderIndex + 1) + " is not finished yet.";
            }
        } else {
            return "Invalid order index.";
        }
    }
}
