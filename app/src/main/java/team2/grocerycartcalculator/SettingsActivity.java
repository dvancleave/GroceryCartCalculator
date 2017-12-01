package team2.grocerycartcalculator;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

import java.util.Locale;

import team2.grocerycartcalculator.db.Budget;

public class SettingsActivity extends AppCompatActivity {

    // Used to get our budget. This is the date for the current month
    long date;
    Budget currBudget;
    EditText budgetField;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Intent intent = getIntent();
        date = intent.getLongExtra(MainActivity.BUDGET_EXTRA, -1);
        currBudget = StartLoadActivity.database.getBudget(date);
        if(currBudget == null)
            System.out.println("Budget hasn't been set. What do??");
        budgetField = findViewById(R.id.settingsBudget);
        budgetField.setText(String.format(Locale.US, "%.2f", (double)(currBudget.getTotal()) / 100));
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        String budgetString = budgetField.getText().toString();
        double budget = currBudget.getTotal();
        if(!budgetString.isEmpty() && !budgetString.equals("."))
            budget = Double.valueOf(budgetString) * 100;
        currBudget.setTotal((int)(budget));
        budgetField.setText("");
        StartLoadActivity.database.saveBudget(currBudget);
        StartLoadActivity.database.saveAll();
    }
}
