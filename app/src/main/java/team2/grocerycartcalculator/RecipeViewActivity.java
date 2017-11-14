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

import team2.grocerycartcalculator.team2.grocerycartcalculator.db.Database;

public class RecipeViewActivity extends AppCompatActivity {

    ArrayList<String> recipeList = new ArrayList<String>();
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
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String recipeName = (String) listView.getItemAtPosition(position);
                //Edit the recipe, which needs to load its own data from the database
                startListActivity(recipeName);
            }
        });
        //TODO: Load the recipe list from the database
        //Load up the database for the generic query

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, recipeList);
        listView.setAdapter(adapter);

        //Do the search if applicable
        handleIntent(getIntent());
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
        recipeList.add("New recipe");
        adapter.notifyDataSetChanged();
    }

    public void startListActivity(String queryName)
    {
        Intent intent = new Intent(this, GroceryListSelectActivity.class);
        intent.putExtra("QueryName", queryName);
        startActivity(intent);
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            //use the query to search your data somehow
        }
    }
}
