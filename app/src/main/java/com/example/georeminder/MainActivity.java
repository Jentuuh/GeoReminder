package com.example.georeminder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jentevandersanden
 * This class provides the integration of Geofences within the application.
 */
public class MainActivity extends AppCompatActivity {

    private GeofencingClient geofencingClient;
    private List<Geofence> geofenceList;
    private PendingIntent geofencePendingIntent;
    private AppDatabase reminderDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        geofenceList = new ArrayList<Geofence>();

        // Initialize the database
        reminderDatabase = AppDatabase.getReminderDatabase(this);

        //Add a test ReminderEntity to the database, with ID "002" and "003" (if there's already
        // a ReminderEntity with either of these ID's this line is ignored.
        DatabasePopulator.addTestReminder(reminderDatabase);

        // Ask the user for permission to use their location, if it wasn't granted already
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted (yet)
            // We ask the user for permission
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    Constants.MY_PERMISSIONS_REQUEST_USE_LOCATION);

            // MY_PERMISSIONS_REQUEST_USE_LOCATION is a constant that contains the requestcode of the permission
        } else {
            // Permission is granted, we can start using the Location services
            initializeGeofence();

        }
    }

    /**
     * Method that will be executed after the user answers the permission request
     * @param requestCode : The same code that is passed from the callback requestPermissions() method
     * @param permissions : The permissions that need to be checked for
     * @param grantResults : The result array (the answers of the user)
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        switch (requestCode){
            case Constants.MY_PERMISSIONS_REQUEST_USE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if(grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // PERMISSION GRANTED
                    initializeGeofence();

                } else {
                    // PERMISSION DENIED
                    makeToast("No permission for location services.");
                }
            }
        }

    }


    /**
     * Method that initializes and creates our test Geofence, used in onCreate() if permission for
     * location services were already granted, or in onRequestPermissionsResult if they are granted
     * right after the permission request.
     */
    private void initializeGeofence(){
            // Creating instance of a Geofencing client (necessary to access the location APIs)
            geofencingClient = LocationServices.getGeofencingClient(this);

            // Sample data, Reminder fetched from our database (with ID '002')
            Reminder testreminder = new Reminder("002", reminderDatabase.reminderDAO().getMessage("002"),
                    reminderDatabase.reminderDAO().getLongitude("002"), reminderDatabase.reminderDAO().getLongitude("002"));

            // Adds a geofence to our list of geofences
            createGeofence(testreminder);

            // We add our list of created geofence-objects to the application
            geofencingClient.addGeofences(getGeofencingRequest(), getGeofencePendingIntent())
                    .addOnSuccessListener(this, new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // Geofences were added
                            // Display this with a Toast
                            makeToast("Geofence was added!");
                        }
                    })
                    .addOnFailureListener(this, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Failed to add geofences
                            // Display this with a Toast
                            makeToast("Failed to add geofence!");
                        }
                    });
        }

        private void makeToast(CharSequence toastmessage){
            Context context = getApplicationContext();
            int duration = Toast.LENGTH_LONG;
            Toast.makeText(context, toastmessage, duration).show();
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


    /**
     * This method starts (or updates) a GeofenceBroadcastReceiver, that handles all the
     * Geofence transitions at runtime.
     * @return PendingIntent: a PendingIntent object that represents the BroadcastReceiver
     */
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
