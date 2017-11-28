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
    SwipeableTextAdapter adapter;
    View rootView;
    SearchView searchBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_view);
        /*
        StartLoadActivity.database.addGroceryList("1", 0L, true);
        StartLoadActivity.database.addGroceryList("2", 0L, true);
        StartLoadActivity.database.addGroceryList("3", 0L, true);
        StartLoadActivity.database.addGroceryList("4", 0L, true);
        //*/

        rootView = findViewById(R.id.recipeRoot);
        searchBar = findViewById(R.id.recipeSearchView);
        searchBar.clearFocus();
        rootView.requestFocus();

        final ListView listView = findViewById(R.id.recipeListView);

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
        adapter = new SwipeableTextAdapter(this, recipeList);
        adapter.setOnDeleteListener(new onDeleteListener()
                    {
                        @Override
                        public void onDelete(int position) {
                            int id = gl.get(position).getID();
                            gl.remove(position);
                            System.out.println("Removed recipe with id " + id);
                            //TODO: remove recipe from database
                        }
                    });
        /*
         * The listview cannot handle click events since the item itself can handle them. So, we
         * need to have the item handle the event for us. The item will call this if it detects a
         * clicking motion
         */
        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = listView.getPositionForView(v);
                int groceryListID = gl.get(position).getID();
                //Edit the recipe, which needs to load its own data from the database
                startListActivity(groceryListID);
            }
        });
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
        GroceryList ngl = StartLoadActivity.database.addGroceryList("New Recipe", 0L, true);
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
