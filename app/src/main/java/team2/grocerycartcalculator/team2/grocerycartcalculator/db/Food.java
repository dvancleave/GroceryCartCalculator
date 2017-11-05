package team2.grocerycartcalculator.team2.grocerycartcalculator.db;

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

    // Fields
    private int id; // DB table id
    private String name; // name of food item
    private int price; // price in pennies

    // Constructors
    protected Food(int id, String name, int price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    // Getters
    protected int getID() {
        return id;
    }
    public String getName() {
        return name;
    }
    public int getPrice() {
        return price;
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }
    public void setPrice(int price) {
        this.price = price;
    }
}
