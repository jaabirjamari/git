package Observer;

import Domain.MenuItem;
import Domain.Size;
import Domain.Customization;
import java.util.List;

public interface PricingStrategy { 

    double calculatePrice(MenuItem baseItem, Size size, List<Customization> customizations); 

} 