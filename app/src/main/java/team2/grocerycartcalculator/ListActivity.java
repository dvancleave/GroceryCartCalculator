package team2.grocerycartcalculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //activity set-up
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        //instantiating variables
        database = new Database(getApplicationContext());
        listview = (ListView) findViewById(R.id.item_list);
        checkoutButton = (Button) findViewById(R.id.checkout_button);
        groceryListID = getIntent().getIntExtra("QueryName", -2);
        groceryList = database.getGroceryListByID(groceryListID);
        //make sure grocerylist exists
        if(groceryList != null){
            ArrayList<Food> Foodlist = new ArrayList<Food>(groceryList.getFoodQuantities().keySet());
            ArrayList<String> FoodStrings = new ArrayList<String>();
            isRecipe = groceryList.isRecipe();
            //hide checkout button if viewing a recipe
            if(isRecipe){
                checkoutButton.setVisibility(View.GONE);
            }

            //populating arraylist
            for(int i =0; i<Foodlist.size(); i++){
                FoodStrings.add(Foodlist.get(i).getName());
            }
            //creating and setting adapter
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list_list_item, FoodStrings);
            listview.setAdapter(adapter);
        }
    }
}
