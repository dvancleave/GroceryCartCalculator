package team2.grocerycartcalculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

import team2.grocerycartcalculator.db.GroceryList;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ListView listView = findViewById(R.id.testListView);
        ArrayList<GroceryList> recipes = new ArrayList<GroceryList>(StartLoadActivity.database.getRecipes());
        ArrayList<String> texts = new ArrayList<>();
        for(GroceryList g : recipes)
            texts.add(g.getName());
        SwipeableTextAdapter adapter = new SwipeableTextAdapter(this, texts);
        listView.setAdapter(adapter);
    }
}
