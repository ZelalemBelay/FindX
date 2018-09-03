package com.findx.zeelo.findx;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MainActivity extends AppCompatActivity
{

    DatabaseReference databaseRefPeople;
    DatabaseReference databaseRefTarget;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView list  = findViewById(R.id.mainList);
//        list.setAdapter(new SimpleAdapter());

        databaseRefPeople = FirebaseDatabase.getInstance().getReference("People");
        databaseRefTarget = FirebaseDatabase.getInstance().getReference("CurrentTarget");

        databaseRefPeople.child("mk").setValue(new Person("mk", new Coordinate("lat", "longt")));
        databaseRefTarget.setValue("mk");

        databaseRefTarget.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Person person = getCurrentPersonCoordinates("mk");
                if(person!=null) {
                    System.out.println(person.getCoordinate().getLatitude());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    Person person;
    public Person getCurrentPersonCoordinates(final String personId) {

        databaseRefPeople.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                person  = new ObjectMapper().convertValue(dataSnapshot.child(personId).getValue(), Person.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return person;
    }
}
