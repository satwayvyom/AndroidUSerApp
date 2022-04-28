package com.vyom.gpstrackersatway;

public class MyListData {
    private String description,imei;
    public MyListData(String description,String imei)
    {
        this.description = description;
        this.imei=imei;
    }
    public String getDescription() {
        return description;
    }
    public String getImei() {
        return imei;
    }
    public void setDescription(String description) {
        this.description = description;
    }
}
