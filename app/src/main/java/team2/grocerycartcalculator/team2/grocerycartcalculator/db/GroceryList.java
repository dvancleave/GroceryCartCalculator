package team2.grocerycartcalculator.team2.grocerycartcalculator.db;

import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * Holds data for grocery lists (lists of Food objects)
 * Protected fields are for DB use only
 */

public class GroceryList {

    // Table column names (indicated by '_' suffix)
    protected static final String _ID = "list_id";
    protected static final String _NAME = "name";
    protected static final String _DATE = "date_for";
    protected static final String _TOTAL_PRICE = "total_price";
    // Used in other tables
    protected static String _TAG = "recipe_tag";

    // Fields
    private int id; // DB table id
    private String name; // name of grocery list
    private Calendar date; // date the list was created for
    private Map<Food, Integer> foods; // list of foods w/ their respective quantities
    private int totalPrice; // total price of food items on list
    private HashSet<String> tags; // list of descriptive tags for this recipe (assuming it's a recipe)

    // Constructors
    protected GroceryList(int id, String name, Calendar date, int totalPrice) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.foods = new HashMap<>();
        this.totalPrice = totalPrice;
        this.tags = new HashSet<>();
    }

    // Getters
    public int getID() {
        return id;
    }
    public String getName() {
        return name;
    }
    public Calendar getDate() {
        return date;
    }
    public Map<Food, Integer> getFoodQuantities() {
        return foods;
    }
    public int getTotalPrice() {
        return totalPrice;
    }
    public HashSet<String> getTags() {
        return tags;
    }
    public int getQuantity(Food food) {
        return foods.containsKey(food) ? foods.get(food) : 0;
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }
    public void setDate(Calendar date) {
        this.date = date;
    }

    // Add/remove food items (returns the old quantity)
    public int addFood(Food food, int quantity) {
        int old = foods.containsKey(food) ? foods.get(food) : 0;
        if (quantity == 0) removeFood(food);
        else foods.put(food, quantity);

        return old; // Return old quantity (0 if not in list)
    }
    public int removeFood(Food food) {
        Integer old = foods.remove(food);

        return old != null ? old : 0; // Return old quantity (0 if not in list)
    }

    // Add/remove tags (returns boolean indicating whether tag was added/removed)
    public boolean addTag(String tag) {
        return tags.add(tag.toLowerCase());
    }
    public boolean removeTag(String tag) {
        return tags.remove(tag.toLowerCase());
    }

    // Returns whether this recipe (assuming it is one) has the given tag
    public boolean hasTag(String tag) {
        return tags.contains(tag.toLowerCase());
    }

    // Set/get whether this GroceryList is being used as a recipe
    public void setRecipe(boolean isRecipe) {
        if (isRecipe != isRecipe()) { // if state changed
            totalPrice = isRecipe ? -1 : 0;
        }
    }
    public boolean isRecipe() {
        return totalPrice == -1;
    }

    // Update total price based on prices/quantities of items
    public void recalculateTotalPrice() {
        totalPrice = 0;

        for (Map.Entry<Food, Integer> e : foods.entrySet()) {
            totalPrice += e.getKey().getPrice() * e.getValue(); // add price * quantity to total
        }
    }

}
