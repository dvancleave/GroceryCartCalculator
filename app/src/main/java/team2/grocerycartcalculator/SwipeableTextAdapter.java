package team2.grocerycartcalculator;


import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class SwipeableTextAdapter extends ArrayAdapter<String>{

    private Context context;
    private ArrayList<String> values;
    private onDeleteListener deleteListener;
    private View.OnClickListener onClickListener;

    public SwipeableTextAdapter(Context context, ArrayList<String> values) {
        super(context, -1, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public int getCount() {
        return values.size();
    }

    @Override
    public String getItem(int pos) {
        return values.get(pos);
    }

    public void setOnDeleteListener(onDeleteListener listener)
    {
        deleteListener = listener;
    }

    public void setOnClickListener(View.OnClickListener listener)
    {
        onClickListener = listener;
    }

    @Override
    @NonNull
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
        if(convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.swipeable_text_delete_template, parent, false);
        }
        final SwipeableLayout swipeableLayout = convertView.findViewById(R.id.swipeableLayout);
        TextView textView = convertView.findViewById(R.id.stText);
        textView.setText(values.get(position));

        Button delete = convertView.findViewById(R.id.stDelete);
        final SwipeableTextAdapter adapter = this;
        delete.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //do something
                swipeableLayout.changeToNotSwiped();
                adapter.values.remove(position); //or some other task
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