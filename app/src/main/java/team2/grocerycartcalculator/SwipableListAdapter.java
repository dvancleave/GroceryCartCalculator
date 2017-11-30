package team2.grocerycartcalculator;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import team2.grocerycartcalculator.db.Food;

public class SwipableListAdapter extends ArrayAdapter<List_Item> {
    private Context context;
    private ArrayList<List_Item> values;
    private ArrayList<String> unitslist;
    private onDeleteListener deleteListener;
    private View.OnClickListener onClickListener;

    public SwipableListAdapter(Context context, ArrayList<List_Item> values){
        super(context, -1, values);
        this.context = context;
        this.values = values;
        unitslist = new ArrayList<String>();
        unitslist.add("lbs");
        unitslist.add("grams");
    }

    public void setOnDeleteListener(onDeleteListener listener)
    {
        deleteListener = listener;
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
        TextView quantityView = convertView.findViewById(R.id.quantity_text);
        Spinner unitsView = convertView.findViewById(R.id.units_spinner);
        TextView priceView = convertView.findViewById(R.id.price_text);
        ArrayAdapter<String> unitsAdapter = new ArrayAdapter<String>(getContext(), R.layout.support_simple_spinner_dropdown_item, unitslist);

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
                //remove task
                adapter.values.remove(position);

                if(deleteListener != null)
                    deleteListener.onDelete(position);
                adapter.notifyDataSetChanged();
            }
        });
        swipeableLayout.setDeleteButton(delete);
        swipeableLayout.setOnClickListener(onClickListener);
        return convertView;
    }

}