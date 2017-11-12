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

public class RecipeViewActivity extends AppCompatActivity {

    ArrayList<String> recipeList = new ArrayList<String>();
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_view);
        final ListView listView = findViewById(R.id.recipeListView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String recipeName = (String) listView.getItemAtPosition(position);
                //Edit the recipe, which needs to load its own data from the database
                startListActivity(recipeName);
            }
        });
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, recipeList);
        listView.setAdapter(adapter);
        //overridePendingTransition(R.anim.slide_right, R.anim.slide_right);
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
}
