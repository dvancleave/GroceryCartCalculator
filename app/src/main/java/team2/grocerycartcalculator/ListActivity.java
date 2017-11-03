package team2.grocerycartcalculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class ListActivity extends AppCompatActivity {
    ListView itemsView;
    ArrayList<String> itemNames = new ArrayList<String>();

    void fillExampleNames(){
        for(int i=0; i<5; i++){
            itemNames.add("List"+Integer.toString(i));
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        itemsView = (ListView) findViewById(R.id.item_list);

        fillExampleNames();
        ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
        String listHashName = "itemName";
        for(int i=0; i<itemNames.size();i++){
            HashMap<String, String> map = new HashMap<String, String>();
            map.put(listHashName, itemNames.get(i));
            data.add(map);
        }
        int resource = R.layout.list_list_item;
        String[] from = {listHashName};
        int[] to = {R.id.List_name};
        SimpleAdapter adapter = new SimpleAdapter(this, data, resource, from, to);
        itemsView.setAdapter(adapter);
    }
}
