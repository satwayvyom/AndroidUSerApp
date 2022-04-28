package com.vyom.gpstrackersatway;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class History_Date_Recyclerview extends AppCompatActivity {


    String StartDate;
    String EndDate;
    String IMEInumber;
    Date StartDate1;
    Date EndDate1;

    List<MyListData> HistoryDateLsit = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history__date__recyclerview);
        SimpleDateFormat sdf = new SimpleDateFormat();
        StartDate = getIntent().getStringExtra("StartDate");
        EndDate = getIntent().getStringExtra("EndDate");
        IMEInumber = getIntent().getStringExtra("imei");
//        try {
//            StartDate1=sdf.parse(StartDate);
//            EndDate1=sdf.parse(EndDate);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        StartDate=sdf.format(StartDate1);
//        EndDate=sdf.format(EndDate1);

        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        String dt = StartDate;  // Start date

        Calendar c = Calendar.getInstance();
        String output = "",day,month,year;
        int i=0;
        System.out.println(StartDate+"-"+EndDate);
        while(!(output.equals(EndDate)) ) {
            try {
                c.setTime(sdf1.parse(dt));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            c.add(Calendar.DATE, i);
            output = sdf1.format(c.getTime());
            MyListData myListData = new MyListData(output,IMEInumber);
            HistoryDateLsit.add(myListData);
            System.out.println(output);
            i++;
        }

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        MyListAdapter adapter = new MyListAdapter(HistoryDateLsit);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
}

