package team2.grocerycartcalculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {
    CalendarView calendar;
    ListView listsList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        calendar = (CalendarView) findViewById(R.id.calendarView);

    }


}
