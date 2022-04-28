package com.vyom.gpstrackersatway;

public class VehicleData {

    private String vehicle_no,IMEI,status,longitude,latitude,mode,distance,speed,address;


    public VehicleData( String vehicle_no,String IMEI,String mode,String longitude,String latitude,String distance,String speed,String address){
        this.vehicle_no = vehicle_no;
         this.IMEI = IMEI;
         this.mode = mode;
         this.longitude=longitude;
         this.latitude=latitude;
         this.distance=distance;
         this.speed=speed;
         this.address=address;

    }
public  String getaddress()
{
    return  address;
}

    public String getVehiclenumber(){
        return  vehicle_no;
    }
    public void setVehiclenumber()

    {
        this.vehicle_no = vehicle_no;
    }
    public  String getimei(){
        return  IMEI;
    }
    public void setimei(){

        this.IMEI = IMEI;
    }
    public String getMode()
    {
        return  mode;
    }
    public void setMode()

    {
        this.mode = mode;
    }
    public String getlongitude(){
        return  longitude;
    }
    public void setlongitude()
    {
        this.longitude = longitude;
    }
    public String getLatitude(){
        return  latitude;
    }
    public void setLatitude(){this.latitude = latitude;}
    public String getStatus(){return status;}
    public void  setStatus(){this.status = status;}
    public String getDistance(){return distance;}
    public void  setDistance(){this.distance = distance;}
    public String getSpeed(){return speed;}
}