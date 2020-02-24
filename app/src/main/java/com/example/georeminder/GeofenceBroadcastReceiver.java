package com.example.georeminder;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;


import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofenceStatusCodes;
import com.google.android.gms.location.GeofencingEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jentevandersanden
 * This class is the Geofence implementation of the BroadcastReceiver class, a
 */
public class GeofenceBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "GeofenceBroadcast";

    @Override
    public void onReceive(Context context, Intent intent) {
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if(geofencingEvent.hasError()) {
            String errorMessage = GeofenceStatusCodes.getStatusCodeString(geofencingEvent.getErrorCode());
            Log.e(TAG, errorMessage);
            return;
        }

        // Get the transition type.
        int geofenceTransition = geofencingEvent.getGeofenceTransition();

        // Test if the reported transition is the transition we're looking for (in our case
        // we're looking only for entering events)
        if(geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER){

            // Get a list of the geofences that were triggered by this particular event.
            List<Geofence> triggeredGeofences = geofencingEvent.getTriggeringGeofences();

            // Get the transition details in a String
            String geofenceTransitionDetails = getGeofenceTransitionDetails(geofenceTransition,triggeredGeofences);

            // Send a notification + log the transition details
            /** All notifications are sent */
            for(int i = 0; i < triggeredGeofences.size(); i++) {
                // Get every triggered geofence's ID
                String geoID = (triggeredGeofences.get(i)).getRequestId();
                // Get the reminder message, based on the ID
                String message = AppDatabase.getReminderDatabase(context).reminderDAO().getMessage(geoID);
                // Show the notification
                showNotification(context, message);
            }

            Log.i(TAG, geofenceTransitionDetails);
        }
        else {
            // Log the occured error
            Log.e(TAG, "Invalid transition type");
        }
    }



    /**
     * Gets all the details on a certain transition (together with all the ID's of the triggered
     * geofences affected by this transition)
     * @param geofenceTransition : The transition that took place
     * @param triggered_geofences : The geofences that were triggered by a certain event
     * @return : Returns a string with the information described in the description.
     */
    private String getGeofenceTransitionDetails(int geofenceTransition, List<Geofence> triggered_geofences){

        String geofenceTransitionString = getTransitionString(geofenceTransition);

        ArrayList<String> triggeredGeofencesIDlist = new ArrayList<>();
        for (Geofence geofence : triggered_geofences){
            triggeredGeofencesIDlist.add(geofence.getRequestId());
        }
        // Puts all the ID's into a string with a ',' as a delimiter
        String triggeredGeofencesID = TextUtils.join(", ", triggeredGeofencesIDlist);

        return geofenceTransitionString + ": " + triggeredGeofencesID;

    }

    /**
     * This method creates the PendingIntent that belongs to the notification, builds
     * the notification and shows it on screen.
     * @param context : The context of the activity that caused the notification
     */
    private void showNotification(Context context, String message){
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
                new Intent(context, MainActivity.class), 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, Constants.GEOFENCE_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_map)
                .setContentTitle("GeoReminder")
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        builder.setContentIntent(contentIntent);
        builder.setDefaults(Notification.DEFAULT_SOUND);
        builder.setAutoCancel(true);
        NotificationManagerCompat notifManager = NotificationManagerCompat.from(context);
        notifManager.notify(1, builder.build());
    }


    /**
     * Translates the transition into a human-readable string (to be used in the details)
     * @param transitionType : The type of transition that took place (constant in Geofence)
     * @return : Returns a human-readable string describing the transition
     */
    private String getTransitionString(int transitionType){
        switch (transitionType) {
            case Geofence.GEOFENCE_TRANSITION_ENTER:
                return "Entered a Geofence";
            case Geofence.GEOFENCE_TRANSITION_EXIT:
                return "Exited a Geofence";
             default:
                 return "Unknown transition";
        }
    }
}
