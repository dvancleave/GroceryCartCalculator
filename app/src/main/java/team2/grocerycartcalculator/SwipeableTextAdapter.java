package team2.grocerycartcalculator;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class SwipeableTextAdapter extends ArrayAdapter<String>{

    private Context context;
    private ArrayList<String> values;

    public SwipeableTextAdapter(Context context, ArrayList<String> values) {
        super(context, -1, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.swipeable_text_delete_template, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.stText);
        textView.setText(values.get(position));
        // change the icon for Windows and iPhone
        String s = values.get(position);

        return rowView;
    }
}