package team2.grocerycartcalculator;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import java.util.ArrayList;

import team2.grocerycartcalculator.db.Food;

public class ItemDatabaseViewActivity extends AppCompatActivity {

    ArrayList<String> itemNameList = new ArrayList<String>();
    ArrayList<Food> itemList;
    ArrayAdapter<String> adapter;
    View rootView;
    SearchView searchBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_database_view);

        rootView = findViewById(R.id.idRoot);
        searchBar = findViewById(R.id.idSearch);
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

    }

    @Override
    protected void onResume( )
    {
        super.onResume();
        rootView.requestFocus();
    }

    public void addItemButton(View view)
    {
        Food food = StartLoadActivity.database.addFood("Apple", 0);
        itemNameList.add(food.getName());
        itemList.add(food);
        adapter.notifyDataSetChanged();
    }

    public void filterBySearchQuery(String query)
    {
        //TODO: updte list by query
        itemList = new ArrayList<>(StartLoadActivity.database.searchFoods(query));
        itemNameList.clear();
        for (Food g : itemList)
            itemNameList.add(g.getName());
        adapter.notifyDataSetChanged();
    }
}
