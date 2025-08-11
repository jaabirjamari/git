// CustomerController supports CustomerOrderingView
import com.cafe.Domain.MenuItem;
import com.cafe.Domain.Order;
import com.cafe.Domain.OrderItem;
import com.cafe.Domain.Size;
import com.cafe.Domain.Customization;
import com.cafe.System.MenuCatalog;
import com.cafe.System.OrderQueue;
import com.cafe.System.CafeSystem; // Facade or main system class
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CustomerController {

    private final CafeSystem cafeSystem;
    private Order currentOrder;
    private final DoubleProperty totalPrice = new SimpleDoubleProperty(0.0);

    public CustomerController(CafeSystem cafeSystem) {
        this.cafeSystem = cafeSystem;
        this.currentOrder = new Order(new Customer("Guest"), new ArrayList<>());
    }

    // --- Methods to provide data to the View ---

    /** Fetches menu items, filtered by category. */
    public List<MenuItem> getMenuItemsByCategory(String category) {
        return cafeSystem.getAllMenuItems().stream()
                .filter(item -> item.getCategory().equalsIgnoreCase(category))
                .collect(Collectors.toList());
    }

    /** Provides the observable list of items for the order summary. */
    public List<String> getOrderSummaryItems() {
        return currentOrder.getItems().stream()
                .map(OrderItem::toString) // Assuming OrderItem has a useful toString()
                .collect(Collectors.toList());
    }

    /** Provides an observable property for the total price. */
    public DoubleProperty totalPriceProperty() {
        return totalPrice;
    }

    // --- Methods to handle View actions ---

    /** Adds a new item to the current order. */
    public void addToOrder(MenuItem menuItem, Size size, List<Customization> customizations) {
        OrderItem orderItem = new OrderItem(menuItem, size);
        customizations.forEach(orderItem::addCustomization);
        currentOrder.addItem(orderItem);
        totalPrice.set(currentOrder.calculateTotal());
    }

    /** Clears the current order. */
    public void clearOrder() {
        this.currentOrder = new Order(new Customer("Guest"), new ArrayList<>());
        totalPrice.set(0.0);
    }

    /** Submits the current order to the system. */
    public void placeOrder() {
        if (!currentOrder.getItems().isEmpty()) {
            cafeSystem.addOrder(currentOrder);
            clearOrder(); // Reset for the next order
        }
    }

    // --- Methods to handle menu bar actions ---

    public void saveOrdersRequested() {
        // Logic to save orders to a file or database
        cafeSystem.saveOrders();
    }

    public void loadOrdersRequested() {
        // Logic to load orders from a file or database
        cafeSystem.loadOrders();
    }
}
