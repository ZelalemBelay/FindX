package com.findx.zeelo.findx.model;

public class TargetPerson {

    String name;
    Coordinate coordinate;
    String status;

    public TargetPerson(String name, String status, Coordinate coordinate) {
        this.name = name;
        this.coordinate = coordinate;
        this.status = status;
    }

    public TargetPerson(String name, String status) {
        this.name = name;
        this.coordinate = coordinate;
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public TargetPerson() {
    }

    public String getName() {
        return name;
    }

    public void setName(String id) {
        this.name = id;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }
}
