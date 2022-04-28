package com.vyom.gpstrackersatway;

public class HistoryData {

    String startDate, endDate,startAddress, endAddress,city1,city2 ,Distance;

    public HistoryData(String startDate, String endDate, String city1, String city2, String startAddress, String endAddress,String Distance) {
        this.startDate = startDate;
        this.city1 = city1;
        this.city2 = city2;
        this.endDate = endDate;
        this.startAddress = startAddress;
        this.Distance = Distance;
        this.endAddress = endAddress;
    }
public String getStartDate()
{
    return startDate;
}
    public String getDistance()
    {
        return Distance;
    }

public String getEndDate(){
        return endDate;
    }
public String getStartAddress(){
    return startAddress;
}
public String getEndAddress(){
    return endAddress;
}
public String getStartCity(){return city1;}
public String getEndCity(){return city2;}

}
