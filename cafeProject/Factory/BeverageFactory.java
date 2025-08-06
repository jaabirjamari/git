package Factory;

import Domain.Beverage;
import Domain.MenuItem;
import Domain.BeverageType;  // import your enum
import java.util.Map;

public class BeverageFactory extends MenuItemFactory { 

    @Override
    public MenuItem createMenuItem(String name, String description, double basePrice, Map<String, Object> additionalParams) { 
        Object obj = additionalParams.get("beverageType");
        if (!(obj instanceof BeverageType)) {
            throw new IllegalArgumentException("Invalid or missing 'beverageType' parameter");
        }
        BeverageType type = (BeverageType) obj;
        return new Beverage(name, description, basePrice, type);
    } 
}
