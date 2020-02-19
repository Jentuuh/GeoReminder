package com.example.georeminder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;

/**
 * @author jentevandersanden
 * This class provides the integration of Geofences within the application.
 */
public class MainActivity extends AppCompatActivity {

    private GeofencingClient geofencingClient;
    private ArrayList<Geofence> geofenceList;
    private PendingIntent geofencePendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Ask the user for permission to use their location, if it wasn't granted already
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED){

            // Permission is not granted (yet)
            // We ask the user for permission
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    Constants.MY_PERMISSIONS_REQUEST_USE_LOCATION);

            // MY_PERMISSIONS_REQUEST_USE_LOCATION is a constant that contains the requestcode of the permission
        }
        else {
            // Permission is granted, we can start using the Location services
            // Creating instance of a Geofencing client (necessary to access the location APIs)
            geofencingClient = LocationServices.getGeofencingClient(this);

            // Sample data
            Reminder testreminder = new Reminder("001", "You just entered the area!", 5.391212, 50.925665);

            // Adds a geofence to our list of geofences
            createGeofence(testreminder);

            // We add our list of created geofence-objects to the application
            geofencingClient.addGeofences(getGeofencingRequest(), getGeofencePendingIntent())
                    .addOnSuccessListener(this, new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // Geofences were added
                            // TODO: make toast or dialog to notify this
                        }
                    })
                    .addOnFailureListener(this, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Failed to add geofences
                            // TODO: make toast or dialog to notify this
                        }
                    });
        }

    }



    /** This method creates a Geofence object (using the data of a Reminder object) and adds it to
     *  the list of Geofence objects in the application
     *
     * @pre reminder != null
     * @post The list of geofences that this activity contains now contains the newly created geofence object.
     * */
    private void createGeofence(Reminder reminder){
        geofenceList.add(new Geofence.Builder()
                .setRequestId(reminder.getGeofenceID())
                .setCircularRegion(
                        reminder.getLatitude(),
                        reminder.getLongitude(),
                        Constants.GEOFENCE_RADIUS_IN_METERS)
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER).build());
    }

    /** Specifies how the geofences will monitor and trigger certain events (we defined the transition
     * types in the createGeofence method above)
     *
     * @return GeofencingRequest : returns the object that specifies the list of geofences
     * to be monitored and how the geofence notifications should be reported
     */
    private GeofencingRequest getGeofencingRequest(){
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        // This makes sure that the device gets notified when it's in the geofence's radius when it's created
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofences(geofenceList);
        return builder.build();
    }

    private PendingIntent getGeofencePendingIntent(){
        // Reuse the PendingIntent if we already have it.
        if(geofencePendingIntent != null){
            return geofencePendingIntent;
        }
        Intent intent = new Intent(this, GeofenceBroadcastReceiver.class);
        // FLAG_UPDATE_CURRENT is used so we get the same pending intent back when calling addGeofences()
        // and removeGeofences().
        geofencePendingIntent = PendingIntent.getBroadcast(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        return geofencePendingIntent;
    }


}
