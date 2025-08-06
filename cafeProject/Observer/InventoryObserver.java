package Observer;

public interface InventoryObserver extends Observer { 
    void onInventoryChanged(String ingredientName, int newLevel); 
    void onLowStock(String ingredientName, int currentLevel); 
} 