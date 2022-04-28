package com.vyom.gpstrackersatway;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HistoryData_Adapter extends RecyclerView.Adapter<HistoryData_Adapter.ViewHolder> {


    private List<HistoryData> historyData;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public TextView startDATE, endDATE, startAddress, endAddress, Avgspeed,Distance;

        public ViewHolder(View view) {
            super(view);
            startDATE = (TextView) view.findViewById(R.id.startTime);
            endDATE = (TextView) view.findViewById(R.id.endTime);
            startAddress = (TextView) view.findViewById(R.id.startPoint);
            endAddress = (TextView) view.findViewById(R.id.endPoint);
            Distance= (TextView) view.findViewById(R.id.topSpeed);
        }

        public TextView getTextView() {
            return textView;

        }
    }


    public HistoryData_Adapter(List<HistoryData> dataSet) {
        historyData = dataSet;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.trip_details, viewGroup, false);

        return new ViewHolder(view);


    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        HistoryData HD = historyData.get(position);
        viewHolder.startDATE.setText(HD.getStartDate());
        viewHolder.endDATE.setText(HD.getEndDate());
        viewHolder.startAddress.setText(HD.getStartCity());
        viewHolder.endAddress.setText(HD.getEndCity());
        viewHolder.Distance.setText(HD.getDistance());
    }

    // Return the size of your dataset (invoked by the layout manager)
   @Override
    public int getItemCount() {
        return historyData.size();
    }
}


