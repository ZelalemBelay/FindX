package com.findx.zeelo.findx;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import com.findx.zeelo.findx.registration.FireBaseUserInserter;
import com.findx.zeelo.findx.registration.MySharedPreferences;
import com.findx.zeelo.findx.service.FirebaseStatusChangeListenerService;
import com.findx.zeelo.findx.validator.FireBaseCurrentTargetChecker;

public class MainActivity extends AppCompatActivity {

    public FireBaseCurrentTargetChecker currentTargetChecker;
    public MySharedPreferences mySharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentTargetChecker = new FireBaseCurrentTargetChecker(this.getApplicationContext());
        mySharedPreferences = new MySharedPreferences(this.getApplicationContext());

        if(mySharedPreferences.isFirstTimeLogin()) {
            displayDialog();
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.e("ERROR", "Permission Issues");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},123);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},123);

            return;
        }
        attemptToInsertToFireBase();
        Intent serviceIntent = new Intent(this, FirebaseStatusChangeListenerService.class);
        startService(serviceIntent);
    }

    public void attemptToInsertToFireBase() {
        new FireBaseUserInserter(this).insertToFirebaseDB();
    }

    private void displayDialog() {
        final EditText usE = new EditText(this);
        new AlertDialog.Builder(this)
                .setTitle("Enter Name: ")
                .setMessage("Please provide username:")
                .setView(usE)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String un = usE.getText().toString();
                        mySharedPreferences.insertToSharedPreferences(un);
                        attemptToInsertToFireBase();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                })
                .show();
    }

}
