package team2.grocerycartcalculator;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import team2.grocerycartcalculator.team2.grocerycartcalculator.db.Food;
import team2.grocerycartcalculator.team2.grocerycartcalculator.db.GroceryList;

/**
 * Created by Charles on 11/13/2017.
 */

public class list_adapter extends ArrayAdapter<GroceryList> {
    GroceryList grocerylist;
    ArrayList<Food> foods;
    public list_adapter(Context context, int textViewResourceId, ArrayList<GroceryList> objects){
        super(context, textViewResourceId, objects);
        grocerylist = objects.get(0);
        foods = new ArrayList<Food>(grocerylist.getFoodQuantities().keySet());

    }

    @Override
    public int getCount() {
        int count = foods.size();
        return count;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.item_list_item, null);
        TextView nameView = (TextView) v.findViewById(R.id.name_view);
        EditText quantityText = (EditText) v.findViewById(R.id.quantity_text);
        EditText unitsText = (EditText) v.findViewById(R.id.units_text);
        TextView priceView = (TextView) v.findViewById(R.id.price_view);
        Food currentFood = foods.get(position);

        nameView.setText(currentFood.getName());
        quantityText.setText(Integer.toString(grocerylist.getQuantity(currentFood)));
        unitsText.setText("N/A");
        priceView.setText(Integer.toString(currentFood.getPrice()));
        return v;
    }
}
