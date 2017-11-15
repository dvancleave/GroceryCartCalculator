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
import java.util.List;

import team2.grocerycartcalculator.team2.grocerycartcalculator.db.GroceryList;

public class GroceryListSelectActivity extends AppCompatActivity {
    ArrayList<String> nameList = new ArrayList<>();
    List<GroceryList> gl;
    ArrayAdapter<String> adapter;
    View rootView;
    long date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grocery_list_select);
        rootView = findViewById(R.id.recipeRoot);

        //This works confirmed 14/11/17
        Intent intent = getIntent();
        date = intent.getLongExtra(MainActivity.GLSA_INTENT_EXTRA, -1);
        if(date > -1)
        {
            //Do stuff here
        }

        final ListView listView = findViewById(R.id.gls_listview);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int groceryListID = gl.get(position).getID();
                //Edit the recipe, which needs to load its own data from the database
                startListActivity(groceryListID);
            }
        });
        gl =  MainActivity.database.getGroceryLists();
        for(GroceryList g : gl)
            nameList.add(g.getName());
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, nameList);
        listView.setAdapter(adapter);
    }

    public void addGroceryList(View view)
    {
        // Will finish this when we get the right methods from the database
        //MainActivity.database.addGroceryList("New Grocery List", date);
    }

    public void startListActivity(int listID)
    {
        Intent intent = new Intent(this, ListActivity.class);
        intent.putExtra("QueryName", listID);
        startActivity(intent);
    }
}
