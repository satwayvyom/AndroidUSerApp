package com.vyom.gpstrackersatway;

public class VehicleDistance {
    public String VehicleNum;
    public Double Distance;

    Double UpdateDistance(Double NewDistance)
    {
        Distance+=NewDistance;
        return Distance;
    }
    String getVehicleNum()
    {
        return VehicleNum;
    }
}

