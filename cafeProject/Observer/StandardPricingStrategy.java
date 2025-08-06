package Observer;

import Domain.MenuItem;
import Domain.Size;
import Domain.Customization;
import java.util.List;

public class StandardPricingStrategy implements PricingStrategy {

    @Override
    public double calculatePrice(MenuItem baseItem, Size size, List<Customization> customizations) {
        double total = baseItem.getBasePrice();

        if (size != null) {
            total = size.applyPriceModifier(total);
        }

        if (customizations != null) {
            for (Customization custom : customizations) {
                total += custom.getExtraCost();
            }
        }

        return total;
    }
}
