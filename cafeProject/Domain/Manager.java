package Domain;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class Manager extends Barista {

    // Constructor
    public Manager(String username, String password, String role, String fullName) {
        super(username, password, Staff.MANAGER, fullName); // Calls Barista constructor
        // Do NOT manually override role here; it's better to handle via constructor if needed
    }

    // Overridden methods
    @Override
    public List<String> getPermissions() {
        List<String> permissions = new ArrayList<>(super.getPermissions());
        permissions.addAll(Arrays.asList("MANAGE_MENU", "MANAGE_INVENTORY", "VIEW_SALES"));
        return permissions;
    }

    // Manager-specific methods
    public boolean canManageMenu() {
        return true;
    }

    public boolean canManageInventory() {
        return true;
    }

    public boolean canViewSales() {
        return true;
    }
}
