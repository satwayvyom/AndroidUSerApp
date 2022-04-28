package com.vyom.gpstrackersatway;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyStop_Adapter extends RecyclerView.Adapter<MyStop_Adapter.ViewHolder> {


    private List<MyStop_Data> MyStop_Data;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public TextView endDATE, endAddress;

        public ViewHolder(View view) {
            super(view);
            endDATE = (TextView) view.findViewById(R.id.time);
            endAddress = (TextView) view.findViewById(R.id.location);
        }

        public TextView getTextView() {
            return textView;
        }
    }


    public MyStop_Adapter(List<MyStop_Data> dataSet) {
        MyStop_Data = dataSet;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.activity_stop_card, viewGroup, false);

        return new ViewHolder(view);
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        MyStop_Data HD = MyStop_Data.get(position);
        viewHolder.endDATE.setText(HD.getEndDate());
        viewHolder.endAddress.setText(HD.getEndCity());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return MyStop_Data.size();
    }
}


