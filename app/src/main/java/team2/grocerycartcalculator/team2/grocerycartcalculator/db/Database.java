package team2.grocerycartcalculator.team2.grocerycartcalculator.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Database Manager
 * =====================
 * Used for saving, loading, storing & running queries on grocery data
 * Uses an SQLite database
 */

public class Database extends SQLiteOpenHelper {

    // Name & version of SQLite database
    private static final String NAME = "groceries";
    private static final int VERSION = 2;

    // Table names (indicated by '_' suffix)
    private static String _FOODS = "foods";
    private static String _LISTS = "lists";
    private static String _LIST_FOODS = "list_foods";
    private static String _FOOD_TAGS = "food_tags";
    private static String _BUDGETS = "budget";

    // The actual database
    private SQLiteDatabase db;

    // Loaded database info (keys refer to IDs for each data structure)
    private Map<Integer, Food> foods = new HashMap<>();
    private Map<Integer, GroceryList> lists = new HashMap<>();
    private List<Budget> budgets = new ArrayList<>();

    // Constructor
    public Database(Context context) {
        super(context, NAME, null, VERSION);

        db = getWritableDatabase(); // Create/open DB & store the pointer
        loadAll(); // Load all data from DB file into memory
    }

    // Create the SQLite database
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create table for foods
        db.execSQL("CREATE TABLE IF NOT EXISTS " + _FOODS + "(" +
                Food._ID + " INTEGER PRIMARY KEY," +
                Food._NAME + " TEXT," +
                Food._PRICE + " INTEGER)");

        // Create table for grocery lists
        db.execSQL("CREATE TABLE IF NOT EXISTS " + _LISTS + "(" +
                GroceryList._ID + " INTEGER PRIMARY KEY," +
                GroceryList._NAME + " TEXT," +
                GroceryList._DATE + " BIGINT," +
                GroceryList._TOTAL_PRICE + " INTEGER)");

        // Create table for grocery list foods and their quantities
        db.execSQL("CREATE TABLE IF NOT EXISTS " + _LIST_FOODS + "(" +
                GroceryList._ID + " INTEGER," +
                Food._ID + " INTEGER," +
                Food._QUANTITY + " REAL," +
                "PRIMARY KEY (" + Food._ID + "," + GroceryList._ID + "))");

        // Create table for food tags
        db.execSQL("CREATE TABLE IF NOT EXISTS " + _FOOD_TAGS + "(" +
                Food._ID + " INTEGER," +
                Food._TAG + " TEXT," +
                "PRIMARY KEY (" + Food._ID + "," + Food._TAG + "))");

        // Create table for monthly budgets
        db.execSQL("CREATE TABLE IF NOT EXISTS " + _BUDGETS + "(" +
                Budget._YEAR + " INTEGER," +
                Budget._MONTH + " INTEGER," +
                Budget._TOTAL + " INTEGER," +
                Budget._SPENT + " INTEGER," +
                "PRIMARY KEY (" + Budget._YEAR + "," + Budget._MONTH + "))");
    }

    // Upgrade from version v1 to version v2
    @Override
    public void onUpgrade(SQLiteDatabase db, int v1, int v2) {
        // Drop old tables
        db.execSQL("DROP TABLE IF EXISTS " + _FOODS);
        db.execSQL("DROP TABLE IF EXISTS " + _LISTS);
        db.execSQL("DROP TABLE IF EXISTS " + _LIST_FOODS);
        db.execSQL("DROP TABLE IF EXISTS " + _FOOD_TAGS);
        db.execSQL("DROP TABLE IF EXISTS " + _BUDGETS);

        // Re-create them
        onCreate(db);
    }

    /*
        Getters & Setters
     */

    // Gets every previously loaded Food instance in list form
    public List<Food> getFoods() {
        return new ArrayList<>(foods.values());
    }

    // Gets every previously loaded GroceryList instance in list form
    public List<GroceryList> getGroceryLists() {
        return new ArrayList<>(lists.values());
    }

    // Gets a food item by its name (must be the exact name, ignoring case)
    public Food getFoodByName(String name) {
        for (Food f : foods.values()) {
            if (f.getName().equalsIgnoreCase(name)) return f;
        }
        return null;
    }

    // Gets a list of food items w/ all of the given tag(s)
    public List<Food> getFoodsByTag(String... tags) {
        List<Food> list = new ArrayList<>();
        List<String> tagList = Arrays.asList(tags);

        for (Food f : foods.values()) {
            if (f.getTags().containsAll(tagList)) list.add(f);
        }

        return list;
    }

    // Gets a list of grocery lists w/ all of the given tag(s) in their food lists
    public List<GroceryList> getGroceryListsByTag(String... tags) {
        List<GroceryList> list = new ArrayList<>();
        List<String> tagList = Arrays.asList(tags);

        for (GroceryList gl : lists.values()) {
            if (gl.getAllTags().containsAll(tagList)) list.add(gl);
        }

        return list;
    }

    // Gets the budget for a specific year and month (or simply a timestamp or calendar object), adding a row to the table if needed
    public Budget getBudget(int year, int month) {
        // Find preexisting budget
        for (Budget b : budgets) {
            if (b.getYear() == year && b.getMonth() == month) return b;
        }

        // If not found, create one & return it
        ContentValues vals = new ContentValues();

        vals.put(Budget._YEAR, year);
        vals.put(Budget._MONTH, month);
        vals.put(Budget._TOTAL, 0);
        vals.put(Budget._SPENT, 0);

        db.insert(_BUDGETS, null, vals);
        Budget budget = new Budget(year, month, 0, 0);

        budgets.add(budget);
        return budget;
    }
    public Budget getBudget(Calendar month) {
        return getBudget(month.get(Calendar.YEAR), month.get(Calendar.MONTH) + 1);
    }
    public Budget getBudget(long timeInMillis) {
        Calendar month = Calendar.getInstance();
        month.setTimeInMillis(timeInMillis);
        return getBudget(month);
    }


    /*
        Adders & Removers
     */

    // Add row to foods table, enforcing lowercase names; returns Food instance
    public Food addFood(String name, int price) {
        Food food;

        // Only add food if no food exists w/ that name
        if ((food = getFoodByName(name)) == null) {
            name = name.toLowerCase(); // Enforce lowercase on food names
            ContentValues vals = new ContentValues();

            vals.put(Food._NAME, name);
            vals.put(Food._PRICE, price);

            int id = (int) db.insert(_FOODS, null, vals);
            foods.put(id, food);
        } else {
            food.setPrice(price);
        }

        return food;
    }

    // Add row to grocery lists table; returns GroceryList instance
    public GroceryList addGroceryList(String name, Calendar date) {
        ContentValues vals = new ContentValues();

        vals.put(GroceryList._NAME, name);
        vals.put(GroceryList._DATE, date.getTimeInMillis());
        vals.put(GroceryList._TOTAL_PRICE, 0);

        int id = (int) db.insert(_LISTS, null, vals);
        GroceryList list = new GroceryList(id, name, date, 0);

        lists.put(id, list);
        return list;
    }

    // Remove row from foods table; returns whether a removal occurred
    public boolean deleteFood(int id) {
        db.delete(_FOODS, Food._ID + "=" + id, null);
        return foods.remove(id) != null;
    }
    public boolean deleteFood(Food food) {
        return deleteFood(food.getID());
    }

    // Remove row from grocery lists table; returns whether a removal occurred
    public boolean deleteGroceryList(int id) {
        db.delete(_LISTS, GroceryList._ID + "=" + id, null);
        return lists.remove(id) != null;
    }
    public boolean deleteGroceryList(GroceryList list) {
        return deleteGroceryList(list.getID());
    }

    // Remove row from grocery lists table; returns

    /*
        Loaders & Savers
        (NOTE: only savers need to be used outside of this class; loading is handled in onOpen())
     */

    // Load everything into memory from database
    private void loadAll() {
        // Iterate through foods & add to list
        Cursor cursor = db.rawQuery("SELECT * FROM " + _FOODS, null);

        if (cursor.moveToFirst()) do {
            Food food = new Food(
                    cursor.getInt(0),       // ID
                    cursor.getString(1),    // Name
                    cursor.getInt(2)        // Price
            );
            foods.put(food.getID(), food);

            // Iterate through list of tags for each food item
            Cursor c2 = db.rawQuery("SELECT * FROM " + _FOOD_TAGS +
                    " WHERE " + Food._ID + "=" + food.getID(), null);

            if (c2.moveToFirst()) do {
                food.addTag(c2.getString(1));
            } while (c2.moveToNext());

            c2.close();
        } while (cursor.moveToNext());

        cursor.close();


        // Iterate through grocery lists & add to list of lists
        cursor = db.rawQuery("SELECT * FROM " + _LISTS, null);

        if (cursor.moveToFirst()) do {
            Calendar date = Calendar.getInstance();
            date.setTimeInMillis(cursor.getLong(2));

            GroceryList list = new GroceryList(
                    cursor.getInt(0),       // ID
                    cursor.getString(1),    // Name
                    date,                      // Date
                    cursor.getInt(3)        // Total price
            );
            lists.put(list.getID(), list);

            // Iterate through list of food items for each grocery list
            Cursor c2 = db.rawQuery("SELECT * FROM " + _LIST_FOODS +
                    " WHERE " + GroceryList._ID + "=" + list.getID(), null);

            if (c2.moveToFirst()) do {
                Food food = foods.get(c2.getInt(1));
                if (food != null) list.addFood(food, c2.getInt(2));
            } while (c2.moveToNext());

            c2.close();
        } while (cursor.moveToNext());

        cursor.close();
    }

    // Saves everything from memory into database
    public void saveAll() {
        for (Food f : getFoods()) {
            saveFood(f);
        }
        for (GroceryList gl : getGroceryLists()) {
            saveGroceryList(gl);
        }
        for (Budget b : budgets) {
            saveBudget(b);
        }
    }

    // Save specific food item to DB
    public void saveFood(Food food) {
        // Update basic info first
        ContentValues vals = new ContentValues();

        vals.put(Food._NAME, food.getName());
        vals.put(Food._PRICE, food.getPrice());

        db.update(_FOODS, vals, Food._ID + "=" + food.getID(), null);

        // Next, delete old tags from food item
        db.delete(_FOOD_TAGS, Food._ID + "=" + food.getID(), null);

        // Finally, add new tags to food item
        db.beginTransaction();
        try {
            vals = new ContentValues();
            for (String t : food.getTags()) {
                vals.put(Food._ID, food.getID());
                vals.put(Food._TAG, t);

                db.insert(_LIST_FOODS, null, vals);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    // Save specific grocery list to DB
    public void saveGroceryList(GroceryList list) {
        // Update basic info first
        ContentValues vals = new ContentValues();

        vals.put(GroceryList._NAME, list.getName());
        vals.put(GroceryList._DATE, list.getDate().getTimeInMillis());
        vals.put(GroceryList._TOTAL_PRICE, list.getTotalPrice());

        db.update(_LISTS, vals, GroceryList._ID + "=" + list.getID(), null);

        // Next, delete old food items from list
        db.delete(_LIST_FOODS, GroceryList._ID + "=" + list.getID(), null);

        // Finally, add new food items to list
        db.beginTransaction();
        try {
            vals = new ContentValues();
            for (Map.Entry<Food, Integer> e : list.getFoodQuantities().entrySet()) {
                vals.put(GroceryList._ID, list.getID());
                vals.put(Food._ID, e.getKey().getID());
                vals.put(Food._QUANTITY, e.getValue());

                db.insert(_LIST_FOODS, null, vals);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    // Save specific monthly budget to DB
    public void saveBudget(Budget budget) {
        ContentValues vals = new ContentValues();

        vals.put(Budget._TOTAL, budget.getTotal());
        vals.put(Budget._SPENT, budget.getSpent());

        db.update(_BUDGETS, vals,
                Budget._YEAR + "=" + budget.getYear()
                + " AND " + Budget._MONTH + "=" + budget.getMonth(), null);
    }

}
