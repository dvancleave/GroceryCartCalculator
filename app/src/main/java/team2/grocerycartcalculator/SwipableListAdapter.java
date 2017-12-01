package team2.grocerycartcalculator;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

import team2.grocerycartcalculator.db.Database;
import team2.grocerycartcalculator.db.GroceryList;

public class SwipableListAdapter extends ArrayAdapter<List_Item> {
    private Context context;
    private ArrayList<List_Item> values;
    private ArrayList<String> unitslist;

    private View.OnClickListener onClickListener;
    private Database database;

    private boolean canEditPrice = false;
    private boolean enabled = true;

    public void setIsEnabled(boolean val)
    {
        enabled = val;
    }

    public void setCanEditPrice(boolean val)
    {
        canEditPrice = val;
    }

    /*
    @Override
    public boolean isEnabled(int position)
    {
        return enabled;
    }
    //*/

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
            convertView = inflater.inflate(R.layout.swipeable_list_delete, parent, false);
        }
        final SwipeableLayout swipeableLayout = convertView.findViewById(R.id.swipeableListLayout);
        TextView nameView = convertView.findViewById(R.id.item_name_text);
        final EditText quantityView = convertView.findViewById(R.id.quantity_text);
        final Spinner unitsView = convertView.findViewById(R.id.units_spinner);
        final EditText priceView = convertView.findViewById(R.id.price_text);
        String unit = values.get(position).getFood().getUnits();
        unitslist = new ArrayList<String>();
        // If you don't clone, it messes with every other view's because you are setting it to the static list, not a copy of it
        if(Units.getSolidList().contains(unit)){
            unitslist = (ArrayList<String>) Units.getSolidList().clone();
        }else if(Units.getLiquidList().contains(unit)){
            unitslist = (ArrayList<String>) Units.getLiquidList().clone();
        }
        unitslist.add("count");

        ArrayAdapter<String> unitsAdapter = new ArrayAdapter<String>(context, R.layout.support_simple_spinner_dropdown_item, unitslist);

        //set textviews texts
        final List_Item currItem = values.get(position);
        if(currItem!= null) {
            nameView.setText(currItem.getName());
            quantityView.setText(currItem.getQuantity());
            unitsView.setAdapter(unitsAdapter);
            double value = Double.valueOf(currItem.getPrice());
            value *= Double.valueOf(currItem.getQuantity());
            //value *= getUnitConv(unitsView);
            priceView.setText(String.format(Locale.US, "%.2f", value));
        }
        Button delete = convertView.findViewById(R.id.list_Item_Delete);

        // If we have disabled editing because we checked out, make sure we set that value!
        swipeableLayout.setEnabled(enabled);
        quantityView.setEnabled(enabled);
        unitsView.setEnabled(enabled);
        delete.setEnabled(enabled);

        // Used only in checkout. Set our price view to be enabled for editing
        priceView.setEnabled(canEditPrice);
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
        /*
        quantityView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus)
                {
                    //Get the new quantity
                    EditText myself = (EditText) v;
                    String newquantity = myself.getText().toString();
                    double newquantitydouble;
                    if (newquantity.equals(".")) {
                        quantityView.setError("Value is not a number");
                        newquantitydouble = 1.0;
                    } else
                        newquantitydouble = newquantity.isEmpty() ? 1.0 : Double.parseDouble(newquantity);

                    //Update the grocery list
                    GroceryList containingList = database.getGroceryListByID(values.get(position).getListID());
                    containingList.setQuantity(values.get(position).getFood(), newquantitydouble);
                    Double value = Double.valueOf(values.get(position).getPrice()) * newquantitydouble;
                    priceView.setText(String.format(Locale.US, "%.2f", value));
                }
            }
        });
        //*/
        //*
        quantityView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //Get the new quantity
                String newquantity = editable.toString();
                double newquantitydouble;
                if(newquantity.equals("."))
                {
                    quantityView.setError("Value is not a number");
                    newquantitydouble = 1.0;
                }
                else
                    newquantitydouble = newquantity.isEmpty() ? 1.0 : Double.parseDouble(newquantity);

                //Update the grocery list
                GroceryList containingList = database.getGroceryListByID(values.get(position).getListID());
                containingList.setQuantity(values.get(position).getFood(), newquantitydouble);
                Double value = Double.valueOf(values.get(position).getPrice()) * newquantitydouble * getUnitConv(unitsView);
                if(!canEditPrice)
                    priceView.setText(String.format(Locale.US, "%.2f", value));
            }
        });

        priceView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String string = s.toString();
                double value;
                if(string.equals("."))
                {
                    priceView.setError("Value is not a number");
                    value = 0;
                }
                else
                    value = string.isEmpty() ? 1.0 : Double.parseDouble(string);
                values.get(position).totalPrice = value * 100;
            }
        });
        //*/
        //*
        // We need to change our price if we change our unit!
        unitsView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int nposition, long id) {
                //Get the new quantity
                String newquantity = quantityView.getText().toString();
                double newquantitydouble;
                if(newquantity.equals("."))
                {
                    quantityView.setError("Value is not a number");
                    newquantitydouble = 1.0;
                }
                else
                    newquantitydouble = newquantity.isEmpty() ? 1.0 : Double.parseDouble(newquantity);

                //Update the grocery list
                Double value = Double.valueOf(values.get(position).getPrice());
                value *= newquantitydouble;
                value *= getUnitConv(unitsView);
                values.get(position).units = (String) unitsView.getSelectedItem();
                if(!canEditPrice)
                    priceView.setText(String.format(Locale.US, "%.2f", value));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //*/
        swipeableLayout.setDeleteButton(delete);
        swipeableLayout.setOnClickListener(onClickListener);
        return convertView;
    }

    private double getUnitConv(Spinner unitSpinner)
    {
        String unitString =  (String) unitSpinner.getSelectedItem();
        return Units.getUnitConv(unitString);
    }
}
