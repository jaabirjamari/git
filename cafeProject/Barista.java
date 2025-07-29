

import java.util.ArrayList;

public class Barista extends User {
    private String userName;                                         // username for barista
    private String password;                                        // password for barista
    private ArrayList<Orders> EntireOrder = new ArrayList<>();            // barista managing orders

    // constructor
    public Barista(String name, String userName, String password) {
        super(name);
        this.userName = userName;
        this.password = password;
    }

    @Override
    // get the role
    public String getRole() {
        return "Barista";
    }

    // Login method
    public boolean login(String username, String password) {

        // checking if username and password 
        // in param is equals to username store in the obj
        return this.userName.equals(username) && this.password.equals(password);
    }

    // assign orders to this barista
    public void setOrders(ArrayList<Orders> EntireOrder) {
        this.EntireOrder = EntireOrder;
    }


    // barista seeing all placed orders
    public String seePlaceOrder() {
        if (EntireOrder.isEmpty()) {
            return "0 placed order";
        }

        StringBuilder curr = new StringBuilder("Current: ");      // current order 
        for (int i = 0; i < EntireOrder.size(); i++) {
            curr.append("Order ").append(i + 1).append(": ").append(EntireOrder.get(i)).append("\n");
        }
        return curr.toString();
    }

    // Update order status to be unfinished currently making or finished
    public String updateOrder(int orderIndex) {
        if (orderIndex >= 0 && orderIndex < EntireOrder.size()) {
            Orders order = EntireOrder.get(orderIndex);
            order.setFinished(true);
            return (orderIndex + 1) + " is finished";
        } else {
            return "no order ";
        }
    }

    // Notify that order is ready for pickup
    public String readyForPickup(int orderIndex) {
        if (orderIndex >= 0 && orderIndex < EntireOrder.size()) {
            Orders order = EntireOrder.get(orderIndex);
            if (order.isFinished()) {
                return (orderIndex + 1) + " ready for pickup ";
            } else {
                return (orderIndex + 1) + " not finished";
            }
        } else {
            return "no order";
        }
    }
}
