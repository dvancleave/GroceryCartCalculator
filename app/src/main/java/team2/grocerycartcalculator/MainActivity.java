package team2.grocerycartcalculator;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    CalendarView calendar;
    ListView listsList;

    //example listNames
    ArrayList<String> listNames = new ArrayList<String>();

    void fillExampleNames(){
        for(int i=0; i<5; i++){
            listNames.add("List"+Integer.toString(i));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        calendar = (CalendarView) findViewById(R.id.calendarView);
        listsList = (ListView) findViewById(R.id.listView);
        //populate example listNames
        fillExampleNames();
        ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
        String listHashName = "listName";
        for(int i=0; i<listNames.size();i++){
            HashMap<String, String> map = new HashMap<String, String>();
            map.put(listHashName, listNames.get(i));
            data.add(map);
        }
        int resource = R.layout.list_list_item;
        String[] from = {listHashName};
        int[] to = {R.id.List_name};
        SimpleAdapter adapter = new SimpleAdapter(this, data, resource, from, to);
        listsList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent listActivity = new Intent(getApplicationContext(), ListActivity.class);
                startActivity(listActivity);
            }
        });
        listsList.setAdapter(adapter);


    }
    //Intent listActivity = new Intent(getApplicationContext(), ListActivity.class);
    //startActivity(listActivity);


}
