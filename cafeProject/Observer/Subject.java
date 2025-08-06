package Observer;

public interface Subject { 
    void addObserver(Observer observer); 
    void removeObserver(Observer observer); 
    void notifyObservers(String changeType, Object data); 
} 

 