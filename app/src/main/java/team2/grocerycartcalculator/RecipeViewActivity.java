package team2.grocerycartcalculator;

import android.app.SearchManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import java.util.ArrayList;
import java.util.List;

import team2.grocerycartcalculator.db.GroceryList;

public class RecipeViewActivity extends AppCompatActivity {

    ArrayList<String> recipeList = new ArrayList<String>();
    ArrayList<GroceryList> gl;
    ArrayAdapter<String> adapter;
    View rootView;
    SearchView searchBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_view);

        rootView = findViewById(R.id.recipeRoot);
        searchBar = findViewById(R.id.recipeSearchView);
        searchBar.clearFocus();
        rootView.requestFocus();

        final ListView listView = findViewById(R.id.recipeListView);
        // Set the listener for our recipe list
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // We get the ids from the list references we store
                int groceryListID = gl.get(position).getID();
                startListActivity(groceryListID);
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
        gl =  new ArrayList<>(StartLoadActivity.database.getRecipes());
        for(GroceryList g : gl)
            recipeList.add(g.getName());
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, recipeList);
        listView.setAdapter(adapter);

    }

    @Override
    protected void onResume( )
    {
        super.onResume();

        searchBar.clearFocus();
        rootView.requestFocus();
    }

    public void startCalendarActivity(View view)
    {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public void addRecipeButton(View view)
    {
        GroceryList ngl = StartLoadActivity.database.addGroceryList("Apple", 0L, true);
        recipeList.add(ngl.getName());
        gl.add(ngl);
        adapter.notifyDataSetChanged();
    }

    public void startListActivity(int listID)
    {
        // We put in the id into the intent so that the view can load it up
        Intent intent = new Intent(this, ListActivity.class);
        intent.putExtra(MainActivity.LA_INTENT_EXTRA, listID);
        startActivity(intent);
    }


    public void filterBySearchQuery(String query)
    {
        gl = new ArrayList<>(StartLoadActivity.database.searchRecipes(query));
        recipeList.clear();
        for (GroceryList g : gl)
            recipeList.add(g.getName());
        adapter.notifyDataSetChanged();
    }
}
