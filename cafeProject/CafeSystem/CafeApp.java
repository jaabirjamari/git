package CafeSystem;

import Domain.Barista;
import Domain.Pastry;
import Domain.Beverage;



import Factory.*;
import System.*;
import java.util.*;


public class CafeApp {

    private MenuCatalog menuCatalog;
    private OrderQueue orderQueue;
    private InventoryManager inventoryManager;

    public CafeApp() {
        menuCatalog = new MenuCatalog();
        orderQueue = new OrderQueue();
        inventoryManager = new InventoryManager();

        seedMenu();
        seedInventory();
    }

    private void seedMenu() {
        menuCatalog.addItem(BeverageFactory.Beverage("Latte", 4.5));
        menuCatalog.addItem(BeverageFactory.Beverage("Espresso", 3.0));
        menuCatalog.addItem(PastryFactory.Pastry("Croissant", 2.5));
    }

    private void seedInventory() {
        inventoryManager.addIngredient("Coffee Beans", 50);
        inventoryManager.addIngredient("Milk", 30);
        inventoryManager.addIngredient("Butter", 20);
    }

    public void displayMenu() {
        for (MenuItem item : menuCatalog.getAllItems()) {
            System.out.println(item);
        }
    }

    public void placeOrder(Order order) {
        if (inventoryManager.canFulfillOrder(order)) {
            inventoryManager.consumeIngredients(order);
            orderQueue.enqueue(order);
            System.out.println("Order placed successfully.");
        } else {
            System.out.println("Order cannot be fulfilled due to low stock.");
        }
    }

    // ✅ THIS is your main method inside CafeSystem.java
    public static void main(String[] args) {
        CafeSystem system = new CafeSystem();

        System.out.println("=== Brew & Bite Menu ===");
        system.displayMenu();

        Customer customer = new Customer("Amina", "amina@example.com");
        List<MenuItem> items = new ArrayList<>();
        items.add(new MenuItem("Latte", 4.5));
        items.add(new MenuItem("Croissant", 2.5));

        Order order = new Order(customer, items);

        System.out.println("\nPlacing order for " + customer.getName() + "...");
        system.placeOrder(order);
    }
}
