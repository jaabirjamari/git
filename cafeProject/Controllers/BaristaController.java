import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import Observer.OrderObserver;
import Domain.Order;

public class BaristaController implements OrderObserver {
    private final CafeSystem cafeSystem;
    private final ObservableList<Order> pendingOrdersObs;
    private final ObservableList<Order> completedOrdersObs;

    public BaristaController(CafeSystem cafeSystem) {
        this.cafeSystem = cafeSystem;
        this.pendingOrdersObs = FXCollections.observableArrayList(cafeSystem.getPendingOrders());
        this.completedOrdersObs = FXCollections.observableArrayList(cafeSystem.getCompletedOrders());
        cafeSystem.registerOrderObserver(this);
    }

    public ObservableList<Order> getPendingOrdersObs() { return pendingOrdersObs; }
    public ObservableList<Order> getCompletedOrdersObs() { return completedOrdersObs; }

    public boolean updateOrderStatus(Order order, OrderStatus status) {
        return cafeSystem.updateOrderStatus(order.getOrderId(), status);
    }

    public boolean completeOrder(Order order) {
        return cafeSystem.completeOrder(order.getOrderId());
    }

    @Override
    public void onOrderAdded(Order order) {
        Platform.runLater(() -> pendingOrdersObs.add(order));
    }

    @Override
    public void onOrderStatusChanged(Order order) {
        Platform.runLater(() -> {
            int idx = pendingOrdersObs.indexOf(order);
            if (idx != -1) pendingOrdersObs.set(idx, order);
        });
    }

    @Override
    public void onOrderCompleted(Order order) {
        Platform.runLater(() -> {
            pendingOrdersObs.remove(order);
            completedOrdersObs.add(order);
        });
    }
}
