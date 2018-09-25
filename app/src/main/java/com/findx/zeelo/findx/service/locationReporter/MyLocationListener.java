package com.findx.zeelo.findx.service.locationReporter;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

import com.findx.zeelo.findx.FireBaseHelper;
import com.findx.zeelo.findx.model.Coordinate;
import com.google.firebase.database.DatabaseReference;

public class MyLocationListener implements LocationListener {
    Coordinate coordinate;
    FireBaseHelper fireBaseHelper;
    DatabaseReference dbRefCoordinates;

    public MyLocationListener(DatabaseReference databaseRefCoordinates) {
        this.dbRefCoordinates = databaseRefCoordinates;
        coordinate = new Coordinate("", "");
        fireBaseHelper = new FireBaseHelper();
    }

    @Override
    public void onLocationChanged(Location location) {
        coordinate.setLatitude(String.valueOf(location.getLatitude()));
        coordinate.setLongitude(String.valueOf(location.getLongitude()));
        fireBaseHelper.insertToFirebase(dbRefCoordinates, coordinate);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
