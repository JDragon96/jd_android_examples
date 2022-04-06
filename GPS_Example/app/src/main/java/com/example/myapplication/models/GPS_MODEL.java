package com.example.myapplication.models;

public class GPS_MODEL {
    private double lat;
    private double lon;
    private int index;

    public void setLat(double lat)
    {
        this.lat = lat;
    }
    public void setLon(double lon)
    {
        this.lon = lon;
    }
    public void setIndex(int idx)
    {
        this.index = idx;
    }

    public double getLat() { return this.lat; }
    public double getLon() { return this.lon; }
    public int getIndex(){ return this.index; }
}
