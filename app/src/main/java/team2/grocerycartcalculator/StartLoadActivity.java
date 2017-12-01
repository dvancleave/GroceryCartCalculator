package team2.grocerycartcalculator;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import team2.grocerycartcalculator.db.Database;

public class StartLoadActivity extends AppCompatActivity {

    public static Database database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_load);
        database = new Database(this);
        Intent startApp = new Intent(this, MainActivity.class);
        startActivity(startApp);

        database.addFood("bread", 499, "count");
        database.addFood("beef", 799, "lbs");
    }
}
