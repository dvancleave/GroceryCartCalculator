package team2.grocerycartcalculator;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Locale;

import team2.grocerycartcalculator.db.Budget;
import team2.grocerycartcalculator.db.Database;
import team2.grocerycartcalculator.db.Food;
import team2.grocerycartcalculator.db.GroceryList;

public class MainActivity extends AppCompatActivity {
    public static final String GLSA_INTENT_EXTRA = "BM_Date";
    public static final String LA_INTENT_EXTRA = "BM_ID";
    public static final String BUDGET_EXTRA = "BM_BUDGET";
    CalendarView calendar;

    /**
     * Sets up the calendar for date selection
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        calendar = findViewById(R.id.calendarView);
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                Calendar selectedDateCal = Calendar.getInstance();
                selectedDateCal.set(year, month, dayOfMonth);
                startGroceryListActivity(selectedDateCal.getTimeInMillis());
            }
        });
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        // Update the budget because that might have changed
        Calendar firstOfMonthCal = Calendar.getInstance();
        // Set the date to the first of the month
        firstOfMonthCal.set(Calendar.DAY_OF_MONTH, 1);
        long date = firstOfMonthCal.getTimeInMillis();
        Budget budget = StartLoadActivity.database.getBudget(date);
        TextView budgetText = findViewById(R.id.budgetText);
        budgetText.setText(String.format(Locale.US, "$%.2f/$%.2f", ((double) budget.getTotal())/100 - ((double) budget.getSpent())/100, ((double) budget.getTotal())/100));
    }
    //1/12/17:1512122369554
    // Start GLSA
    public void startGroceryListActivity(long date)
    {
        Intent intent = new Intent(this, GroceryListSelectActivity.class);
        intent.putExtra(GLSA_INTENT_EXTRA, date);
        Budget selectedMonthBudget = StartLoadActivity.database.getBudget(date);
        startActivity(intent);
        System.out.println("This line was executed");
    }

    // Switches to RV activity, bottom task bar function
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

    public void startSettingsActivity(View view)
    {
        Calendar firstOfMonthCal = Calendar.getInstance();
        // Set the date to the first of the month
        firstOfMonthCal.set(Calendar.DAY_OF_MONTH, 1);
        long date = firstOfMonthCal.getTimeInMillis();
        Intent intent = new Intent(this, SettingsActivity.class);
        intent.putExtra(BUDGET_EXTRA, date);
        startActivity(intent);
    }
}
