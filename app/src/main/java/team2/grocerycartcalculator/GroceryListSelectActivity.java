package team2.grocerycartcalculator;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import team2.grocerycartcalculator.db.GroceryList;

public class GroceryListSelectActivity extends AppCompatActivity {
    ArrayList<String> nameList = new ArrayList<>();
    ArrayList<GroceryList> gl;
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
        gl =  new ArrayList<>(StartLoadActivity.database.getGroceryListsByDay(date));
        for(GroceryList g : gl)
            nameList.add(g.getName());
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, nameList);
        listView.setAdapter(adapter);
    }

    public void addGroceryList(View view)
    {
        // Will finish this when we get the right methods from the database
        GroceryList ngl = StartLoadActivity.database.addGroceryList("New Grocery List", date, false);
        nameList.add(ngl.getName());
        gl.add(ngl);
        adapter.notifyDataSetChanged();
    }

    public void startListActivity(int listID)
    {
        Intent intent = new Intent(this, ListActivity.class);
        intent.putExtra(MainActivity.LA_INTENT_EXTRA, listID);
        startActivity(intent);
    }
}
