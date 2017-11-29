package team2.grocerycartcalculator;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

public class SwipableListAdapter extends ArrayAdapter<List_Item> {
    private Context context;
    private ArrayList<List_Item> values;
    private onDeleteListener deleteListener;
    private View.OnClickListener onClickListener;

    public SwipableListAdapter(Context context, ArrayList<List_Item> values){
        super(context, -1, values);
        this.context = context;
        this.values = values;
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
        EditText quantityView = convertView.findViewById(R.id.quantity_text);
        TextView unitsView = convertView.findViewById(R.id.units_text);
        TextView priceView = convertView.findViewById(R.id.price_text);

        //set textviews texts
        nameView.setText(values.get(position).getName());
        quantityView.setText(values.get(position).getQuantity());
        priceView.setText(values.get(position).getPrice());

        Button delete = convertView.findViewById(R.id.list_Item_Delete);
        final SwipableListAdapter adapter = this;

        delete.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                swipeableLayout.changeToNotSwiped();
                //remove task
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
