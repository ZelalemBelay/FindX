package com.findx.zeelo.findx.model;

public class Coordinate {

    String latitude;
    String longitude;

    public Coordinate(String lattitude, String longitude) {
        this.latitude = lattitude;
        this.longitude = longitude;
    }

    public Coordinate() {
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
