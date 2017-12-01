package team2.grocerycartcalculator;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import team2.grocerycartcalculator.db.Database;
import team2.grocerycartcalculator.db.Food;
import team2.grocerycartcalculator.db.GroceryList;

public class SwipableListAdapter extends ArrayAdapter<List_Item> {
    private Context context;
    private ArrayList<List_Item> values;
    private ArrayList<String> unitslist;

    private View.OnClickListener onClickListener;
    private Database database;

    public SwipableListAdapter(Context context, ArrayList<List_Item> values){
        super(context, -1, values);
        this.context = context;
        this.values = values;
        //unitslist = new ArrayList<String>();
        database = StartLoadActivity.database;
    }



    public void setOnClickListener(View.OnClickListener listener)
    {
        onClickListener = listener;
    }

    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
        if(convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.swipable_list_delete, parent, false);
        }
        final SwipeableLayout swipeableLayout = convertView.findViewById(R.id.swipeableListLayout);
        TextView nameView = convertView.findViewById(R.id.item_name_text);
        EditText quantityView = convertView.findViewById(R.id.quantity_text);
        Spinner unitsView = convertView.findViewById(R.id.units_spinner);
        TextView priceView = convertView.findViewById(R.id.price_text);
        String unit = values.get(position).getFood().getUnits();
        if(Units.getSolidList().contains(unit)){
            unitslist = Units.getSolidList();
        }else if(Units.getLiquidList().contains(unit)){
            unitslist = Units.getLiquidList();
        }else{
            unitslist = new ArrayList<String>();
            unitslist.add("count");
        }
        ArrayAdapter<String> unitsAdapter = new ArrayAdapter<String>(context, R.layout.support_simple_spinner_dropdown_item, unitslist);

        //set textviews texts
        if(values.get(position)!= null) {
            nameView.setText(values.get(position).getName());
            quantityView.setText(values.get(position).getQuantity());
            priceView.setText(values.get(position).getPrice());
            unitsView.setAdapter(unitsAdapter);
        }
        Button delete = convertView.findViewById(R.id.list_Item_Delete);
        final SwipableListAdapter adapter = this;

        delete.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                swipeableLayout.changeToNotSwiped();
                //remove food from database
                GroceryList containingList = database.getGroceryListByID(values.get(position).getListID());
                containingList.removeFood(values.get(position).getFood());
                database.saveGroceryList(containingList);
                adapter.values.remove(position);
                adapter.notifyDataSetChanged();
            }
        });
        quantityView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //save to database
                String newquantity = editable.toString();
                Double newquantitydouble = Double.parseDouble(newquantity);
                GroceryList containingList = database.getGroceryListByID(values.get(position).getListID());
                containingList.setQuantity(values.get(position).getFood(),newquantitydouble );
            }
        });
        swipeableLayout.setDeleteButton(delete);
        swipeableLayout.setOnClickListener(onClickListener);
        return convertView;
    }

}
