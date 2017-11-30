package team2.grocerycartcalculator;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;

import team2.grocerycartcalculator.db.Database;
import team2.grocerycartcalculator.db.Food;
import team2.grocerycartcalculator.db.GroceryList;

public class ListActivity extends AppCompatActivity {


    ListView listview;
    Button checkoutButton;
    Database database;
    EditText nameEdit;
    int groceryListID;
    GroceryList groceryList;
    boolean isRecipe;
    ArrayList<List_Item> foodList;
    SwipableListAdapter adapter;

    protected static String itemIDKey = "BM_ITEMID";
    protected static int itemIDResultKey = 1;

    // Save onPause, which is when this activity goes out of focus or is deleted
    // We save here because we get more time. We don't get a lot of time, if any, for onDelete
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
        listview = findViewById(R.id.item_list);
        checkoutButton =  findViewById(R.id.checkout_button);
        nameEdit = findViewById(R.id.list_name_edit);
        groceryListID = getIntent().getIntExtra(MainActivity.LA_INTENT_EXTRA, -2);
        groceryList = database.getGroceryListByID(groceryListID);
        //make sure grocerylist exists
        checkoutButton.setVisibility(View.GONE);
        nameEdit.setText(groceryList.getName());
        if(groceryList != null){
            isRecipe = groceryList.isRecipe();
            //show checkout button if viewing grocerylist
            if(!isRecipe){
                checkoutButton.setVisibility(View.VISIBLE);
            }
            //make sure food list is empty
            foodList = new ArrayList<List_Item>();
            populateFoodList();
            //creating and setting adapter
            adapter = new SwipableListAdapter(this, foodList);
            listview.setAdapter(adapter);
        }

        checkoutButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                startCheckoutActivity();
            }
        });

       nameEdit.addTextChangedListener(new TextWatcher() {
           @Override
           public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

           }

           @Override
           public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

           }

           @Override
           public void afterTextChanged(Editable editable) {
                String newName = editable.toString();
                //change name in list and save list to database
               groceryList.setName(newName);
               database.saveGroceryList(groceryList);
           }
       });


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
                foodList.add(new List_Item(food, 1, groceryListID));
                adapter.notifyDataSetChanged();
            }
        }
    }
    private void populateFoodList(){
        Map<Food, Integer> listmap =  groceryList.getFoodQuantities();
        ArrayList<Food> foods = new ArrayList<>(listmap.keySet());
        for(int i=0; i<foods.size(); i++){
            foodList.add(new List_Item(foods.get(i), listmap.get(foods.get(i)), groceryListID));
        }

    }
    private void startCheckoutActivity(){
        Intent intent = new Intent(this, CheckoutActivity.class);
        intent.putExtra(MainActivity.LA_INTENT_EXTRA, groceryListID);
        startActivity(intent);
    }
}
