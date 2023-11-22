package com.example.emergencyguardian;


public class Station {
    private String name;
    private double lat;
    private double lon;
    private String icon;

    public Station(){

    }

    public Station(String name,double lat, double lon,String icon){
        this.name=name;
        this.lat=lat;
        this.lon=lon;
        this.icon=icon;
    }

    public String getName() {
        return name;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public String getIcon() {
        return icon;
    }


}
