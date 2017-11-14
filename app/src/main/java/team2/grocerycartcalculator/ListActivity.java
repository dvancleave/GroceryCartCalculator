package team2.grocerycartcalculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import team2.grocerycartcalculator.team2.grocerycartcalculator.db.Database;
import team2.grocerycartcalculator.team2.grocerycartcalculator.db.GroceryList;

public class ListActivity extends AppCompatActivity {

    Calendar currentDate = Calendar.getInstance();
    ListView listview;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        listview = (ListView) findViewById(R.id.item_list);
        currentDate.setTimeInMillis(getIntent().getLongExtra("date", 0));

        //get appropriate grocerylist and add it to this array list
        ArrayList<GroceryList> inputArray = new ArrayList<GroceryList>();
        //inputArray.add(grocerylist);
        list_adapter adapter = new list_adapter(this, R.layout.item_list_item, inputArray);
        listview.setAdapter(adapter);

    }
}
