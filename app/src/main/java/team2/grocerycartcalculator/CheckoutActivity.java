package team2.grocerycartcalculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;

public class CheckoutActivity extends AppCompatActivity {

    private ListView listView;
    private Button finishButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        listView = findViewById(R.id.checkout_listview);
        finishButton = findViewById(R.id.checkout_finish_button);
    }
}
