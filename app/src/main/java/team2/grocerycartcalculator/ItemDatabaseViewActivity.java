package team2.grocerycartcalculator;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;

import team2.grocerycartcalculator.db.Food;

public class ItemDatabaseViewActivity extends AppCompatActivity {

    private ArrayList<String> itemNameList = new ArrayList<String>();
    private ArrayList<Food> itemList;
    private ArrayAdapter<String> adapter;
    private ConstraintLayout rootView;
    private int unitType;
    private static final int UNIT_LIQUID    = 0;
    private static final int UNIT_SOLID     = 1;
    private static final int UNIT_COUNT     = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_database_view);

        rootView = findViewById(R.id.idRoot);

        SearchView searchBar = findViewById(R.id.idSearch);
        rootView.requestFocus();

        final ListView listView = findViewById(R.id.idList);
        // Set the listener for our recipe list
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // We get the ids from the list references we store
                int itemID = itemList.get(position).getID();
                Intent returnIntent = new Intent();
                returnIntent.putExtra(ListActivity.itemIDKey, itemID);
                setResult(RESULT_OK, returnIntent);
                finish();
            }
        });

        // Now set the listener for our search bar
        searchBar.setOnQueryTextListener(
                new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextChange(String newText) {
                        // your text view here
                        return true;
                    }

                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        filterBySearchQuery(query);
                        return true;
                    }
                });

        //Load up the database for the generic, empty query
        itemList =  new ArrayList<>(StartLoadActivity.database.getFoods());
        for(Food f : itemList)
            itemNameList.add(f.getName());
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, itemNameList);
        listView.setAdapter(adapter);

        //Set the extra screens to be invisible so we don't see them

    }

    //Functions for selecting unit type. Glorified wrappers
    public void selectLiquid(View view)
    {
        unitType = UNIT_LIQUID;
        goToStage2();
    }
    public void selectSolid(View view)
    {
        unitType = UNIT_SOLID;
        goToStage2();
    }
    public void selectCount(View view)
    {
        unitType = UNIT_COUNT;
        goToStage2();
    }

    // Complete the add process with a success
    public void finishAdd(View view)
    {
        EditText editText = findViewById(R.id.idAddName);
        String foodName = editText.getText().toString();
        //TODO string checking
        if(foodName.isEmpty())
        {
            // Notify that the string has an error
            editText.setError(getResources().getString(R.string.idEditTextEmptyError));
            // Exit
            return;
        }
        if(foodName.length() > 64)
        {
            // Notify that the string has an error
            editText.setError(getResources().getString(R.string.idEditTextLengthError));
            // Exit
            return;
        }
        Food newFood = StartLoadActivity.database.addFood(foodName, 0);
        itemList.add(newFood);
        itemNameList.add(newFood.getName());

        //TODO Add unit info
        exitAdd(view);
    }

    // Loads up the second stage layout: name, cancel, finish
    private void goToStage2()
    {
        View stage1 = findViewById(R.id.idStage1);
        View stage2 = findViewById(R.id.idStage2);
        stage1.setVisibility(View.INVISIBLE);
        stage2.setVisibility(View.VISIBLE);
    }

    // Starts the mini activity to add a new food item. Starts with stage 1
    public void addItemButton(View view)
    {
        View filter = findViewById(R.id.idDarkFilter);
        View stage1 = findViewById(R.id.idStage1);
        View addItem = findViewById(R.id.idAddLayout);
        filter.setVisibility(View.VISIBLE);
        addItem.setVisibility(View.VISIBLE);
        stage1.setVisibility(View.VISIBLE);
    }

    public void filterBySearchQuery(String query)
    {
        //TODO: update list by query
        itemList = new ArrayList<>(StartLoadActivity.database.searchFoods(query));
        itemNameList.clear();
        for (Food g : itemList)
            itemNameList.add(g.getName());
        adapter.notifyDataSetChanged();
    }

    //Exit the add process
    public void exitAdd(View view)
    {
        View filter = findViewById(R.id.idDarkFilter);
        View addItem = findViewById(R.id.idAddLayout);
        View stage1 = findViewById(R.id.idStage1);
        View stage2 = findViewById(R.id.idStage2);
        filter.setVisibility(View.INVISIBLE);
        addItem.setVisibility(View.INVISIBLE);
        stage1.setVisibility(View.INVISIBLE);
        stage2.setVisibility(View.INVISIBLE);
    }
}
