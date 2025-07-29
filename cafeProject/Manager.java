package CafeOrderProject;

import java.util.ArrayList;

public class Manager extends User {
    private String userName;
    private String password;

    private ArrayList<String> menuItems = new ArrayList<>();
    private ArrayList<String> inventoryItems = new ArrayList<>();

    public Manager(String name, String userName, String password) {
        super(name);
        this.userName = userName;
        this.password = password;
    }

    @Override
    public String getRole() {
        return "Manager";
    }

    public boolean login(String inputUsername, String inputPassword) {
        return this.userName.equals(inputUsername) && this.password.equals(inputPassword);
    }

    public String checkInventory() {
        if (inventoryItems.isEmpty()) {
            return "Inventory is empty.";
        } else {
            return "Inventory in stock: " + inventoryItems.toString();
        }
    }

    public String addToMenu(String item) {
        menuItems.add(item);
        return item + " has been added to the menu.";
    }

    public String removeFromMenu(String item) {
        if (menuItems.contains(item)) {
            menuItems.remove(item);
            return item + " has been removed from the menu.";
        } else {
            return item + " not found in the menu.";
        }
    }

    public String viewMenu() {
        return "Current menu: " + menuItems.toString();
    }

    public void addInventoryItem(String item) {
        inventoryItems.add(item);
    }

    public void removeInventoryItem(String item) {
        inventoryItems.remove(item);
    }

    public ArrayList<String> getMenuItems() {
        return menuItems;
    }

    public ArrayList<String> getInventoryItems() {
        return inventoryItems;
    }
}
