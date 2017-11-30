package team2.grocerycartcalculator;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

import team2.grocerycartcalculator.db.GroceryList;

public class GroceryListSelectActivity extends AppCompatActivity {
    ArrayList<String> nameList = new ArrayList<>();
    ArrayList<GroceryList> gl;
    SwipeableTextAdapter adapter;
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

        final ListView listView = findViewById(R.id.gls_listview);
        gl =  new ArrayList<>(StartLoadActivity.database.getGroceryListsByDay(date));
        for(GroceryList g : gl)
            nameList.add(g.getName());
        adapter = new SwipeableTextAdapter(this, nameList);
        adapter.setOnDeleteListener(new OnDeleteListener()
        {
            @Override
            public void onDelete(int position) {
                int id = gl.get(position).getID();
                gl.remove(position);
                StartLoadActivity.database.deleteGroceryList(id);
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

        // Set the date text at the top
        DateFormat formatter = DateFormat.getDateInstance();
        String dateString = formatter.format(new Date(date));
        TextView dateText = findViewById(R.id.glsaDate);
        dateText.setText(dateString);
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
