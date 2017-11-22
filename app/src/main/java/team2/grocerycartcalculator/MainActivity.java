package team2.grocerycartcalculator;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;

import team2.grocerycartcalculator.db.Database;
import team2.grocerycartcalculator.db.Food;
import team2.grocerycartcalculator.db.GroceryList;

public class MainActivity extends AppCompatActivity {
    public static final String GLSA_INTENT_EXTRA = "BM_Date";
    public static final String LA_INTENT_EXTRA = "BM_ID";
    CalendarView calendar;

    /**
     * Sets up the calendar for date selection
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        calendar = findViewById(R.id.calendarView);
        //overridePendingTransition(R.anim.slide_right, R.anim.slide_left);
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                startGroceryListActivity();
            }
        });
    }

    public void startGroceryListActivity()
    {
        Intent intent = new Intent(this, GroceryListSelectActivity.class);
        long date = calendar.getDate();
        intent.putExtra(GLSA_INTENT_EXTRA, date);
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

    public void startTestActivity(View view)
    {
        Intent intent = new Intent(this, TestActivity.class);
        startActivity(intent);
    }
}
