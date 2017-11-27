package team2.grocerycartcalculator.db;

import java.util.HashSet;

/**
 * Holds data for individual food items
 * Protected fields are for DB use only
 */

public class Food {

    // Table column names (indicated by '_' suffix)
    protected static String _ID = "food_id";
    protected static String _NAME = "name";
    protected static String _PRICE = "price";
    // Used in other tables
    protected static String _QUANTITY = "quantity";
    protected static String _TAG = "food_tag";

    // Fields
    private int id; // DB table id
    private String name; // name of food item
    private int price; // price in pennies
    private HashSet<String> tags; // List of descriptive tags for this food item

    // Constructors
    protected Food(int id, String name, int price) {
        this.id = id;
        this.name = name;
        this.price = price;
        tags = new HashSet<>();
    }

    // Getters
    // Set public for ItemDatabaseViewActivityOld -- Andrew
    public int getID() {
        return id;
    }
    public String getName() {
        return name;
    }
    public int getPrice() {
        return price;
    }
    public HashSet<String> getTags() {
        return tags;
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }
    public void setPrice(int price) {
        this.price = price;
    }

    // Adders & Removers (return value: true = something was changed, false = nothing changed)
    public boolean addTag(String tag) { return tags.add(tag.toLowerCase()); }
    public boolean removeTag(String tag) { return tags.remove(tag.toLowerCase()); }

    // Returns whether this food item has the given tag
    public boolean hasTag(String tag) {
        return tags.contains(tag.toLowerCase());
    }

}
