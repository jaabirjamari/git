package Factory;
import java.util.Map;
import Domain.MenuItem;
import Domain.Pastry;

public class PastryFactory extends MenuItemFactory { 

    public MenuItem createMenuItem(String name, String description, double basePrice, Map<String, Object> additionalParams) { 

        Pastry type = (Pastry) additionalParams.get("pastryType"); 
        String variation = (String) additionalParams.get("variation"); 

        return new Pastry(name, description, basePrice, type, variation); 

    } 

} 