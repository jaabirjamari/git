
import java.util.ArrayList;

public class Customers extends User {
    private ArrayList<Orders> customerOrders;

    public Customers(String name, ArrayList<Orders> customerOrders) {
        super(name);
        if (customerOrders != null) {
            this.customerOrders = customerOrders;
        } else {
            this.customerOrders = new ArrayList<>();
        }
    }

    @Override
    public String getRole() {
        return "Customer";
    }

    public void addOrder(Orders order) {
        customerOrders.add(order);
        System.out.println("Order added successfully.");
    }

    public void deleteOrder(int index) {
        if (index >= 0 && index < customerOrders.size()) {
            customerOrders.remove(index);
            System.out.println("Order removed.");
        } else {
            System.out.println("Invalid order index.");
        }
    }

    public void placeOrder() {
        if (customerOrders.isEmpty()) {
            System.out.println("No orders to place.");
        } else {
            System.out.println("Placing the following orders:");
            for (Orders o : customerOrders) {
                System.out.println(o);
            }
        }
    }

    public ArrayList<Orders> getOrders() {
        return customerOrders;
    }
}
