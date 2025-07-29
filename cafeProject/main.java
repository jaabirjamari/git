import java.util.ArrayList;

public class main {
    public static void main(String[] args) {
        // Create a Manager
        Manager manager = new Manager("Alice", "aliceManager", "pass123");
        System.out.println(manager.getRole() + " " + manager.getName());

        // Manager adds menu items and inventory items
        manager.addToMenu("Latte");
        manager.addToMenu("Croissant");
        manager.addInventoryItem("Coffee Beans");
        manager.addInventoryItem("Flour");
        System.out.println(manager.viewMenu());
        System.out.println(manager.checkInventory());

        // Create a Barista
        Barista barista = new Barista("Bob", "bobBarista", "baristaPass");
        System.out.println(barista.getRole() + " " + barista.getName());

        // Barista login check
        System.out.println("Barista login successful? " + barista.login("bobBarista", "baristaPass"));

        // Create a Customer
        Customers customer = new Customers("Charlie", new ArrayList<>());
        System.out.println(customer.getRole() + " " + customer.getName());

        // Customer places an order
        ArrayList<String> customizations = new ArrayList<>();
        customizations.add("Extra Shot");
        Orders order1 = new Orders(customizations, "Latte", 2, 1, false);
        customer.addOrder(order1);

        // Customer places order (prints)
        customer.placeOrder();

        // Barista gets orders (simulate assigning customer orders to barista)
        ArrayList<Orders> ordersForBarista = new ArrayList<>(customer.getOrders());
        barista.setOrders(ordersForBarista);

        // Barista views orders
        System.out.println(barista.seePlaceOrder());

        // Barista updates order status to finished
        System.out.println(barista.updateOrder(0));

        // Barista checks if order is ready for pickup
        System.out.println(barista.readyForPickup(0));
    }
}
