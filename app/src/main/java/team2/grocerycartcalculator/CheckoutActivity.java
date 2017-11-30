package team2.grocerycartcalculator;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Map;

import team2.grocerycartcalculator.db.Database;
import team2.grocerycartcalculator.db.Food;
import team2.grocerycartcalculator.db.GroceryList;

public class CheckoutActivity extends AppCompatActivity {

    private ListView listView;
    private Button finishButton;
    int groceryListID;
    GroceryList groceryList;
    Database database;
    ArrayList<List_Item> foodList;
    SwipableListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = StartLoadActivity.database;
        setContentView(R.layout.activity_checkout);
        listView = findViewById(R.id.checkout_listview);
        finishButton = findViewById(R.id.checkout_finish_button);
        groceryListID = getIntent().getIntExtra(MainActivity.LA_INTENT_EXTRA, -2);
        groceryList = database.getGroceryListByID(groceryListID);

        foodList = new ArrayList<List_Item>();
        populateFoodList();
        //creating and setting adapter
        adapter = new SwipableListAdapter(this, foodList);
        listView.setAdapter(adapter);

        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchMainActivity();
            }
        });
    }

    private void populateFoodList(){
        Map<Food, Integer> listmap =  groceryList.getFoodQuantities();
        ArrayList<Food> foods = new ArrayList<>(listmap.keySet());
        for(int i=0; i<foods.size(); i++){
            foodList.add(new List_Item(foods.get(i), listmap.get(foods.get(i)), groceryListID));
        }

    }
    private void launchMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
