package Factory;
import java.util.Map;

import Domain.MenuItem;

public abstract class MenuItemFactory { 

    public abstract MenuItem createMenuItem(String name, String description, double basePrice, Map<String, Object> additionalParams); 

     
    public static MenuItemFactory getFactory(String itemType) { 
        switch (itemType.toUpperCase()) { 
            case "BEVERAGE": return new BeverageFactory(); 
            case "PASTRY": return new PastryFactory(); 
            default: throw new IllegalArgumentException("Unknown item type: " + itemType); 

        } 

    } 

} 