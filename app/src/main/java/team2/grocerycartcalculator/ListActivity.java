package team2.grocerycartcalculator;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

import team2.grocerycartcalculator.db.Database;
import team2.grocerycartcalculator.db.Food;
import team2.grocerycartcalculator.db.GroceryList;

public class ListActivity extends AppCompatActivity {


    ListView listview;
    Button checkoutButton;
    Database database;
    int groceryListID;
    GroceryList groceryList;
    boolean isRecipe;
    ArrayList<String> FoodStrings;
    ArrayAdapter<String> adapter;

    protected static String itemIDKey = "BM_ITEMID";
    protected static int itemIDResultKey = 1;

    @Override
    public void onPause()
    {
        super.onPause();
        database.saveGroceryList(groceryList);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //activity set-up
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        //instantiating variables
        database = StartLoadActivity.database;
        listview = (ListView) findViewById(R.id.item_list);
        checkoutButton = (Button) findViewById(R.id.checkout_button);
        groceryListID = getIntent().getIntExtra(MainActivity.LA_INTENT_EXTRA, -2);
        groceryList = database.getGroceryListByID(groceryListID);
        //make sure grocerylist exists
        checkoutButton.setVisibility(View.GONE);
        if(groceryList != null){
            ArrayList<Food> Foodlist = new ArrayList<Food>(groceryList.getFoodQuantities().keySet());
            FoodStrings = new ArrayList<String>();
            isRecipe = groceryList.isRecipe();
            //hide checkout button if viewing a recipe
            if(!isRecipe){
                checkoutButton.setVisibility(View.VISIBLE);
        }

            //populating arraylist
            for(Food food : Foodlist)
                FoodStrings.add(food.getName());

            //creating and setting adapter
            adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, FoodStrings);
            listview.setAdapter(adapter);
        }
    }

    public void addItem(View view)
    {
        Intent intent = new Intent(this, ItemDatabaseViewActivity.class);
        startActivityForResult(intent, itemIDResultKey);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == itemIDResultKey && resultCode == RESULT_OK)
        {
            int id = data.getIntExtra(itemIDKey, -1);
            if(id != -1)
            {
                Food food = database.getFoodByID(id);
                groceryList.addFood(food, 1);
                FoodStrings.add(food.getName());
                adapter.notifyDataSetChanged();
            }
        }
    }
}
