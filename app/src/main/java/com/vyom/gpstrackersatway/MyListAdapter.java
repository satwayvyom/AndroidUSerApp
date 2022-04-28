package com.vyom.gpstrackersatway;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyListAdapter extends RecyclerView.Adapter<MyListAdapter.ViewHolder>{
    private List<MyListData> listdata;
    MyListData DateData;

    // RecyclerView recyclerView;
    public MyListAdapter(List<MyListData> listdata) {
        this.listdata = listdata;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.activity_list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final MyListData myListData = listdata.get(position);
        holder.DateText.setText(listdata.get(position).getDescription());
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(),"click on item: "+myListData.getDescription(),Toast.LENGTH_LONG).show();
            }
        });

        holder.Running.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateData = listdata.get(position);
                Intent i = new Intent(v.getContext(), activity_main1.class);
                i.putExtra("Date", DateData.getDescription());
                i.putExtra("imei", DateData.getImei());
                i.putExtra("mode","RUNNING" );

                v.getContext().startActivity(i);
            }

        });

        holder.Stopped.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateData = listdata.get(position);
                Intent i = new Intent(v.getContext(), stopped_history.class);
                i.putExtra("Date", DateData.getDescription());
                i.putExtra("imei", DateData.getImei());
                i.putExtra("mode", "STOPPED");
                v.getContext().startActivity(i);

            }
        });
    }


    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView DateText;
        Button Running,Stopped;
        public RelativeLayout relativeLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            this.DateText = (TextView) itemView.findViewById(R.id.textView);
            Running = itemView.findViewById(R.id.button4);
            Stopped = itemView.findViewById(R.id.button5);
            relativeLayout = (RelativeLayout)itemView.findViewById(R.id.relativeLayout);
        }
    }
}