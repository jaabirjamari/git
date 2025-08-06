package Observer;
import Domain.Order;

public interface OrderObserver extends Observer { 
    void onOrderAdded(Order order); 
    void onOrderStatusChanged(Order order); 
    void onOrderCompleted(Order order); 
}