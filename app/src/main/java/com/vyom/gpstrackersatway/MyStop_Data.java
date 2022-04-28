package com.vyom.gpstrackersatway;

public class MyStop_Data {

    String  endDate, city,endLat,endLon;  ;

    public MyStop_Data(String endDate, String city,String endLat,String endLon){

        this.endDate = endDate;
        this.city = city;
        this.endLat=endLat;
        this.endLon=endLon;
    }
    public String getEndDate(){
        return endDate;
    }
    public String getEndCity(){
        return city;
    }
    public String getEndLat(){
        return endLat;
    }
    public String getEndLon(){
        return endLon;
    }

}
