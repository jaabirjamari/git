package Domain;

import java.util.Arrays;
import java.util.List;

public class Barista extends Staff {

    // Constructor
    public Barista(String username, String password, String role, String fullName) {
        super(username, password, role "Barista", fullName);
    }

    // Implemented abstract method (from Staff)
    @Override
    public List<String> getPermissions() {
        return Arrays.asList("VIEW_ORDERS", "UPDATE_ORDER_STATUS", "COMPLETE_ORDERS");
    }

    // Barista-specific methods
    public boolean canUpdateOrderStatus(Order order) {
        return order.getStatus() != OrderStatus.FULFILLED;
    }

    public boolean canCompleteOrder(Order order) {
        return order.getStatus() != OrderStatus.FULFILLED;
    }
}
