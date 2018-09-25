package com.findx.zeelo.findx.service;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.findx.zeelo.findx.Constants;
import com.findx.zeelo.findx.MainActivity;
import com.findx.zeelo.findx.model.TargetPerson;
import com.findx.zeelo.findx.validator.FireBaseCurrentTargetChecker;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class FirebaseStatusChangeListenerService extends Service{
    public DatabaseReference databaseRefUsers;
    public FireBaseCurrentTargetChecker fireBaseCurrentTargetChecker;
    public static Context applicationContext;
    Intent service;

    public FirebaseStatusChangeListenerService() {
    }

    public void initailizeListener() {
        databaseRefUsers = FirebaseDatabase.getInstance().getReference(Constants.TARGET_USERS);
        fireBaseCurrentTargetChecker = new FireBaseCurrentTargetChecker(applicationContext);
        service = new Intent(applicationContext, RealTimeLocationReporterService.class);

        databaseRefUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<String, Object> targetPersonMap = (HashMap<String, Object>) dataSnapshot.getValue();
                boolean currentUserChangedToOn = fireBaseCurrentTargetChecker.checkChangedUserIsCurrentUser(targetPersonMap);
                if(currentUserChangedToOn){
                    Log.i("INFO: ", "Initialized ON OFF Listener");
                    applicationContext.startService(service);
            }
                else{
                    applicationContext.stopService(service);
            }
        }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "SERVICE: ", Toast.LENGTH_SHORT).show();
        applicationContext = getApplicationContext();
        initailizeListener();
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
