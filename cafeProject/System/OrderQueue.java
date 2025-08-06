package System;

import Domain.Order;
import Domain.OrderStatus;
import Observer.Observer;
import Observer.OrderObserver;
import Observer.Subject;
import java.util.Queue;
import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;


public class OrderQueue implements Subject { 

    // Attributes 
    private Queue<Order> pendingOrders; 
    private List<Order> completedOrders; 
    private List<OrderObserver> observers; 

    private int nextOrderId; 

    // Constructor 
    public OrderQueue() { 
        pendingOrders = new LinkedList<>(); 
        completedOrders = new ArrayList<>(); 
        observers = new ArrayList<>(); 
        nextOrderId = 1; 
    } 

    // Queue management methods 
    public void addOrder(Order order) { 
        order.setOrderId(nextOrderId++); 
        pendingOrders.offer(order); 
        notifyObservers("ORDER_ADDED", order); 
    } 

    public Order getNextOrder() { 
        return pendingOrders.peek();
        
    } 

    public List<Order> getAllPendingOrders() { 
        return new ArrayList<>(pendingOrders); 
    } 

    public boolean updateOrderStatus(int orderId, OrderStatus newStatus) { 
        for (Order order : pendingOrders) { 
            if (order.getOrderId() == orderId) { 
                order.updateStatus(newStatus); 
                notifyObservers("STATUS_CHANGED", order); 
                return true; 
            } 
        } 
        return false; 
    } 

    public boolean completeOrder(int orderId) { 
        Order order = pendingOrders.stream() 
                .filter(o -> o.getOrderId() == orderId) 
                .findFirst() 
                .orElse(null); 
        if (order != null) { 
            pendingOrders.remove(order); 
            order.updateStatus(OrderStatus.FULFILLED); 
            completedOrders.add(order); 
            notifyObservers("ORDER_COMPLETED", order); 
            return true; 
        } 
        return false; 
    } 

    // Observer pattern methods 

    public void addObserver(Observer observer) { 
        if (observer instanceof OrderObserver) { 
            observers.add((OrderObserver) observer); 
        } 
    } 

    public void removeObserver(Observer observer) { 
        observers.remove(observer); 
    } 

    public void notifyObservers(String changeType, Object data) { 
        Order order = (Order) data; 
        for (OrderObserver observer : observers) { 
            switch (changeType) { 
                case "ORDER_ADDED": 
                    observer.onOrderAdded(order); 
                    break; 
                case "STATUS_CHANGED": 
                    observer.onOrderStatusChanged(order); 
                    break; 
                case "ORDER_COMPLETED": 
                    observer.onOrderCompleted(order); 
                    break; 
            } 
        } 
    } 

    // Getters 
    public List<Order> getCompletedOrders() { 
        return new ArrayList<>(completedOrders); 
    } 
}
