
import java.util.ArrayList;

public class Customers extends User {
    private ArrayList<Orders> customerOrders;           // store customer order in a list 

    // constructor
    public Customers(String name, ArrayList<Orders> customerOrders) {
        super(name);
        this.customerOrders = new ArrayList<>();
    }

    @Override
    // get the role 
    public String getRole() {
        return "Customer";
    }

    // adding order
    public void addOrder(Orders order) {
        customerOrders.add(order);
        System.out.println("order is added");
    }

    // delete order method to delete the order 
    public void deleteOrder(int index) {
        if (index >= 0 && index < customerOrders.size()) {
            customerOrders.remove(index);
            System.out.println("order is removed");
        } else {
            System.out.println("invalid order.");
        }
    }

    // place order method to place order 
    public void placeOrder() {
        if (customerOrders.isEmpty()) {
            System.out.println("no orders placed ");
        } else {
            System.out.println("placed order");
            for (Orders o : customerOrders) {
                System.out.println(o);
            }
        }
    }

    public ArrayList<Orders> getOrders() {
        return customerOrders;
    }
}
