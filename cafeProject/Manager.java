import java.util.ArrayList;

public class Manager extends User {
    private String userName;                                            // username for manager
    private String password;                                            // password for manager
    private ArrayList<String> Product = new ArrayList<>();                  // list of products on the menu
    private ArrayList<String> ProductInventory = new ArrayList<>();         // list of products in the inventory

    // Manager constructor
    public Manager(String name, String userName, String password) {
        super(name);
        this.userName = userName;
        this.password = password;
    }

    @Override
    // getRole method to get the role
    public String getRole() {
        return "Manager";
    }

    // login method
    public boolean login(String username, String password) {
        return this.userName.equals(username) && this.password.equals(password);
    }

    // checking the inventory to see if it’s empty or not
    public String checkInventory() {
        if (ProductInventory.isEmpty()) {
            return "Inventory is empty ";
        } else {
            return "Inventory: " + ProductInventory.toString();
        }
    }

    // manager adding new items to the menu    
    public String addToMenu(String item) {
        Product.add(item);                              // adding item product to the menu
        return item + " added to the menu ";
    }

    // manager removing item from the menu
    public String removeFromMenu(String item) {
        if (Product.contains(item)) {                   // seeing if the product 
            Product.remove(item);
            return item + " removed from the menu";       // removed 
        } else {
            return item + " not found in the menu";       // item is not found
        }
    }

    // viewing the menu
    public String viewMenu() {
        return "the menu: " + Product.toString();
    }

    // adding a item to inventory
    public void addInventoryItem(String item) {
        ProductInventory.add(item);
    }

    // remove item from inventory
    public void removeInventoryItem(String item) {
        ProductInventory.remove(item);
    }

    // getters
    public ArrayList<String> getProduct() {
        return Product;
    }

    public ArrayList<String> getProductInventory() {
        return ProductInventory;
    }
}
