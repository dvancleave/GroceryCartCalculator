package team2.grocerycartcalculator;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;

import team2.grocerycartcalculator.team2.grocerycartcalculator.db.Database;

public class MainActivity extends AppCompatActivity {
    public static Database database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CalendarView calendar = findViewById(R.id.calendarView);
        //overridePendingTransition(R.anim.slide_right, R.anim.slide_left);
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                startGroceryListActivity();
            }
        });
        database = new Database(this);
    }

    public void startGroceryListActivity()
    {
        Intent intent = new Intent(this, GroceryListSelectActivity.class);
        startActivity(intent);
        System.out.println("This line was executed");
    }

    public void startRecipeViewActivity(View view)
    {
        Intent intent = new Intent(this, RecipeViewActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        System.out.println("This line was executed");
    }

}
