package System;
import Domain.Customization;
import Domain.MenuItem;
import Domain.Size;
import java.util.Map;
import java.util.List;

public class MenuCatalog { 

    // Attributes 

    private Map<Integer, MenuItem> menuItems; 

    private List<Size> standardSizes; 

    private List<Customization> standardCustomizations; 

    private int nextItemId; 

     

    // Constructor 

    public MenuCatalog() { 

        menuItems = new HashMap<>(); 

        standardSizes = new ArrayList<>(); 

        standardCustomizations = new ArrayList<>(); 

        nextItemId = 1; 

    } 

     

    // Menu management methods 

    public void addMenuItem(MenuItem item) { 

        item.setItemId(nextItemId++); 

        menuItems.put(item.getItemId(), item); 

    } 

     

    public boolean removeMenuItem(int itemId) { 

        return menuItems.remove(itemId) != null; 

    } 

     

    public boolean updateMenuItem(MenuItem item) { 

        if (menuItems.containsKey(item.getItemId())) { 

            menuItems.put(item.getItemId(), item); 

            return true; 

        } 

        return false; 

    } 

     

    public MenuItem getMenuItem(int itemId) { 

        return menuItems.get(itemId); 

    } 

     

    public List<MenuItem> getAllMenuItems() { 

        return new ArrayList<>(menuItems.values()); 

    } 

     

    public List<MenuItem> getMenuItemsByType(Class<? extends MenuItem> type) { 

        return menuItems.values().stream() 

                .filter(type::isInstance) 

                .collect(Collectors.toList()); 

    } 

     

    // Size and customization management 

    public void addStandardSize(Size size) { 

        standardSizes.add(size); 

    } 

     

    public void addStandardCustomization(Customization customization) { 

        standardCustomizations.add(customization); 

    } 

     

    public List<Size> getStandardSizes() { 

        return new ArrayList<>(standardSizes); 

    } 

     

    public List<Customization> getStandardCustomizations() { 

        return new ArrayList<>(standardCustomizations); 

    } 

     

    // Utility methods 

    public List<MenuItem> getAvailableItems(InventoryManager inventory) { 

        return menuItems.values().stream() 

                .filter(item -> { 

                    Map<String, Integer> required = item.getBaseIngredients(); 

                    return inventory.hasEnoughIngredients(required); 

                }) 

                .collect(Collectors.toList()); 

    } 

}