
import java.util.ArrayList;

public class Orders {
    private ArrayList<String> CustomizeProduct = new ArrayList<>();     // customize product
    private String product;                                             // a string for products in order
    private int size;                                                   // size of the drink (large med and small sizes)
    private int amountOfItems;                                          // amount of item the customer is buying
    private boolean isFinished = false; // status of the order like if its unfinished or is it finished and ready for pick up 

    
    public Orders(ArrayList<String> CustomizeProduct, String product, int size, int amountOfItems, boolean isFinished){
        this.CustomizeProduct = new ArrayList<>();
        this.product = product; 
        this.size = size;
        this.amountOfItems = amountOfItems;
        this.isFinished = false;
    }

    //getters 
    public ArrayList<String> getCustomizeProduct(){
        return CustomizeProduct;
    }

    public String getProduct(){
        return product;
    }

    public int getSize(){
        return size;
    }

    public int getAmountOfItems(){
        return amountOfItems;
    }
    
    public boolean isFinished() {
        return isFinished;
    }

    //setters
    public void setCustomizeProduct (ArrayList<String> CustomizeProduct){
        this.CustomizeProduct = CustomizeProduct;
    }

    /**
    public void setProduct(String Product){
        this.product = product;
    }
    */

    public void setSize(int size){
        this.size = size;
    }

    public void getAmountOfItems(int amountOfItems){
        this.amountOfItems = amountOfItems;
    }

    public void setFinished(boolean finished) {
        isFinished = finished;
    }

    // toString method
    public String toString(){
        return ("Customization: " + CustomizeProduct +
                "Product: " + product +
                "Size: " + size + 
                "Amount of Items" + amountOfItems );
    }


}
