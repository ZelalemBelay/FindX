package com.findx.zeelo.findx.service;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.findx.zeelo.findx.MainActivity;
import com.findx.zeelo.findx.registration.MySharedPreferences;
import com.findx.zeelo.findx.service.locationReporter.MyLocationListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RealTimeLocationReporterService extends Service {
    DatabaseReference databaseRefCoordinates;
    LocationManager locationManager;
    LocationListener locationListener;
    String userName;
    Context applicationContext;

    public RealTimeLocationReporterService() {
    }

    public void startLocationReport() {
        locationManager = (LocationManager) applicationContext.getSystemService(Context.LOCATION_SERVICE);
        userName = new MySharedPreferences(applicationContext).getCurrentUserName();
        databaseRefCoordinates = FirebaseDatabase.getInstance().getReference("LOC_" + userName);
        locationListener = new MyLocationListener(databaseRefCoordinates);

        Toast.makeText(this.getApplicationContext(), "Listening for loc Change", Toast.LENGTH_SHORT).show();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10, locationListener);
    }

    public void stopLocationReport() {
        locationManager.removeUpdates(locationListener);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        applicationContext = getApplicationContext();
        startLocationReport();
        Log.i("INFO: ", "Started Location Report.");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        stopLocationReport();
        Log.i("INFO: ", "Stopped Location Report.");

        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
