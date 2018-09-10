package com.findx.zeelo.findx;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.findx.zeelo.findx.model.Coordinate;
import com.findx.zeelo.findx.model.Person;
import com.findx.zeelo.findx.locationReporter.MyLocationListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class MainActivity extends AppCompatActivity {
    DatabaseReference databaseRefPeople;
    DatabaseReference databaseRefTarget;
    public List<Person> personList = new ArrayList<>();

    LocationManager locationManager;
    Person person;
    public Person selectedUser;

    SharedPreferences userPrefs;

    boolean isAdmin = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userPrefs = getSharedPreferences("username", MODE_PRIVATE);
        String userName = userPrefs.getString("username", "--empty");

        if(userName.contains("Zeelo") || userName.contains("Zelalem") || userName.contains("admin")) {
            isAdmin = true;
            setContentView(R.layout.activity_main);
        }
        else {
//            getWindow().getDecorView().setActivated(false);
            getWindow().getDecorView().getRootView().setVisibility(View.INVISIBLE);
        }

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if(userName.equalsIgnoreCase("--empty")) {
            displayDialog();
        }

        final LocationListener locationListener = new MyLocationListener(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET}, 10);
            }

            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, locationListener);

        ListView list  = findViewById(R.id.mainList);
        databaseRefPeople = FirebaseDatabase.getInstance().getReference("People");
        databaseRefTarget = FirebaseDatabase.getInstance().getReference("CurrentTarget");

        person = new Person(userName, new Coordinate("0", "0"));

        if(isAdmin) {
            addAdapterToListView(list);
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    String selectedId = (String) list.getItemAtPosition(position);
                    System.out.println("selectedUser: "+selectedId + " - currentUserId"+person.getId());
                    databaseRefTarget.setValue(selectedId);

                    getCurrentPersonCoordinates(selectedId);
                    System.out.println("selectedPerson: "+selectedUser);
                }
            });
        }
        databaseRefTarget.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(selectedUser!=null) {
                    System.out.println(selectedUser.getCoordinate().getLatitude());

                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET}, 10);
                        }

                        return;
                    }
                    Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    selectedUser.getCoordinate().setLatitude(location.getLatitude()+"");
                    selectedUser.getCoordinate().setLongitude(location.getLongitude()+"");

                        System.out.println("Writing IDD "+selectedUser.getId()+" - "+ selectedUser.getCoordinate().getLongitude());
                        databaseRefPeople.child(selectedUser.getId()).setValue(selectedUser);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

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
                        SharedPreferences.Editor editor = userPrefs.edit();
                        editor.putString("username", un);
                        editor.apply();
                        Log.i("USERNAME : ", userPrefs.getString("username", "--empty"));
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                })
                .show();
    }

    public List<Person> addAdapterToListView(ListView list) {

        databaseRefPeople.addValueEventListener(new ValueEventListener() {

            @SuppressLint("NewApi")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                dataSnapshot.getChildren().forEach(ds -> {
                    personList.add(new ObjectMapper().convertValue(ds.getValue(), Person.class));
                    System.out.println("added : "+ds.getValue()+" - "+ personList.size());
                });

                list.setAdapter(new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, android.R.id.text1,
                        personList.stream().map(a -> a.getId()).collect(Collectors.toList())));
                personList.clear();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return personList;
    }

    public Person getCurrentPersonCoordinates(final String personId) {

        databaseRefPeople.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                selectedUser  = new ObjectMapper().convertValue(dataSnapshot.child(personId).getValue(), Person.class);
                Toast.makeText(getApplicationContext(),selectedUser.getId()+" : "+ selectedUser.getCoordinate().getLatitude()+" - " + selectedUser.getCoordinate().getLongitude(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return selectedUser;
    }
}
