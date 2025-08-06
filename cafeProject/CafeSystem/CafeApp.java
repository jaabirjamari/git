package CafeSystem;

import Domain.*;
import System.InventoryManager;
import System.MenuCatalog;
import System.OrderQueue;
import Domain.Order;
import Domain.Ingredient;
import Domain.MenuItem;
import Domain.Customer;

import java.util.*;

public class CafeApp {

    public static void main(String[] args) {
        // Set up system components
        InventoryManager inventory = new InventoryManager();
        MenuCatalog menu = new MenuCatalog();
        OrderQueue queue = new OrderQueue();

        // Seed inventory
        inventory.addIngredient(new Ingredient("Espresso", 50));
        inventory.addIngredient(new Ingredient("Milk", 30));
        inventory.addIngredient(new Ingredient("Croissant Dough", 20));

        // Seed menu items
        MenuItem latte = new MenuItem("Latte", "Warm espresso with milk", 4.5);
        latte.addIngredient("Espresso", 1);
        latte.addIngredient("Milk", 2);

        MenuItem croissant = new MenuItem("Croissant", "Buttery pastry", 2.5);
        croissant.addIngredient("Croissant Dough", 1);

        menu.addMenuItem(latte);
        menu.addMenuItem(croissant);

        // Display menu
        System.out.println("=== MENU ===");
        for (MenuItem item : menu.getAllMenuItems()) {
            System.out.println(item);
        }

        // Place an order
        Customer customer = new Customer("Amina", "amina@email.com");
        List<OrderItem> items = new ArrayList<>();
        items.add(new OrderItem(latte));
        items.add(new OrderItem(croissant));

        Order order = new Order(customer, items);

        if (inventory.canFulfillOrder(items)) {
            inventory.consumeIngredients(order.getTotalIngredients());
            queue.addOrder(order);
            System.out.println("\nOrder placed successfully.");
        } else {
            System.out.println("\nOrder could not be placed due to low stock.");
        }

        // Display pending orders
        System.out.println("\n=== Pending Orders ===");
        for (Order o : queue.getAllPendingOrders()) {
            System.out.println(o);
        }
    }
}
