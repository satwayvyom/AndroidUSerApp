package com.vyom.gpstrackersatway;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder>
{
    boolean click = true;
    int position;
    private List<VehicleData> VehicleDataList;
    VehicleData VehileDataObj ;
    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView vehicle_no, imei, status, distance,modetxt,speed,city;
        ImageButton locate ,sharebtn;
        ImageView imageView;

        public MyViewHolder(final View view) {
            super(view);
            locate=view.findViewById(R.id.imageButton2);
            sharebtn = view.findViewById(R.id.sharebtn);
            speed = (TextView) view.findViewById(R.id.modetxt2);
            distance = (TextView) view.findViewById(R.id.modetxt3);
            vehicle_no = (TextView) view.findViewById(R.id.textTitle);
            imei = (TextView) view.findViewById(R.id.imeitxt);
            modetxt = (TextView)view.findViewById(R.id.modetxt);
            city= (TextView)view.findViewById(R.id.modetxt4);
            }
    }
    public Adapter(List<VehicleData> VehicleDataList) {
        this.VehicleDataList = VehicleDataList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_custom_view1, parent, false);

        return new MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        VehileDataObj = VehicleDataList.get(position);

        holder.vehicle_no.setText(VehileDataObj.getVehiclenumber());
        holder.imei.setText(VehileDataObj.getimei());
        holder.modetxt.setText(VehileDataObj.getMode());
        holder.distance.setText(VehileDataObj.getDistance());
        holder.speed.setText(VehileDataObj.getSpeed());
        holder.city.setText(VehileDataObj.getaddress());

        holder.locate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VehileDataObj = VehicleDataList.get(position);
                Intent i = new Intent(v.getContext(), MapView.class);
                i.putExtra("vehicle_no", VehileDataObj.getVehiclenumber());
                i.putExtra("imei", VehileDataObj.getimei());
                v.getContext().startActivity(i);
            }

        });

        holder.sharebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VehileDataObj = VehicleDataList.get(position);
                Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
                whatsappIntent.setType("text/plain");
                whatsappIntent.setPackage("com.whatsapp");
                whatsappIntent.putExtra(Intent.EXTRA_TEXT, VehileDataObj.getaddress());
                try {
                    v.getContext().startActivity(whatsappIntent);
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(v.getContext(), "No Data", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    @Override
    public int getItemCount()

    {
        return VehicleDataList.size();
    }


}




