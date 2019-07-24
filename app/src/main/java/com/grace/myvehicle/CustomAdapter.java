package com.grace.myvehicle;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter<Transaction> implements View.OnClickListener{

    private ArrayList<Transaction> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView code;
        TextView amount;
        TextView phone;
        TextView date;
    }

    public CustomAdapter(ArrayList<Transaction> data, Context context) {
        super(context, R.layout.transaction_row, data);
        this.dataSet = data;
        this.mContext=context;

    }

    @Override
    public void onClick(View v) {

    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Transaction dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.transaction_row, parent, false);
            viewHolder.code = (TextView) convertView.findViewById(R.id.code);
            viewHolder.amount = (TextView) convertView.findViewById(R.id.amount);
            viewHolder.phone = (TextView) convertView.findViewById(R.id.phone);
            viewHolder.date = (TextView)convertView.findViewById(R.id.date);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        viewHolder.code.setText(dataModel.getCode());
        viewHolder.amount.setText("KES " + dataModel.getAmount()+"");
        viewHolder.phone.setText(dataModel.getPhone());
        viewHolder.date.setText(dataModel.getDate());
        // Return the completed view to render on screen
        return convertView;
    }
}