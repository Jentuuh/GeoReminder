package com.example.georeminder;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private GeofencingClient geofencingClient;
    private ArrayList<Geofence> geofenceList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Creating instance of a Geofencing client (necessary to access the location APIs)
        geofencingClient = LocationServices.getGeofencingClient(this);

        // Sample data
        Reminder testreminder = new Reminder("001", "You just entered the area!",5.391212,50.925665);

        // Adds a geofence to our list of geofences
        geofenceList.add(new Geofence.Builder()
                    .setRequestId("INSERT THE ID OF THE GEOFENCE HERE (ALSO SAVED IN THE DATABASE")
                    .setCircularRegion(
                            testreminder.getLatitude(),
                            testreminder.getLongitude(),
                            Constants.GEOFENCE_RADIUS_IN_METERS)
                    .setExpirationDuration(Geofence.NEVER_EXPIRE)
                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER).build());


    }
}
