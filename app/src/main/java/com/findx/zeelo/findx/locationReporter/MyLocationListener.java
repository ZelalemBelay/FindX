package com.findx.zeelo.findx.locationReporter;

import android.app.Activity;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import com.findx.zeelo.findx.MainActivity;

public class MyLocationListener implements LocationListener {
    Activity activity;
    public MyLocationListener(MainActivity a) {
        this.activity = a;
    }
    @Override
    public void onLocationChanged(Location location) {
        System.out.println(location.getLatitude() +" - " + location.getLongitude());
//        Toast.makeText(this.activity, location.getLatitude()+"", Toast.LENGTH_LONG).show();
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
