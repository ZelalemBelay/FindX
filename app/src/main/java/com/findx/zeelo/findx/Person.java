package com.findx.zeelo.findx;

public class Person {

    String id;

    Coordinate coordinate;

    public Person(String id, Coordinate coordinate) {
        this.id = id;
        this.coordinate = coordinate;
    }

    public Person() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }
}
