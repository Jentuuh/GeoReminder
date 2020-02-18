package com.example.georeminder;

/**
 * @author jentevandersanden
 * This class represents the data objects used to save a reminder (also represented in the database)
 */
public class Reminder {
    private String geofenceID;
    private String remindermessage;
    private double longitude;
    private double latitude;

    /** CONSTRUCTOR */

    public Reminder(String geofenceID, String remindermessage, double longitude, double latitude) {
        this.geofenceID = geofenceID;
        this.remindermessage = remindermessage;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    /** GETTERS */

    public String getGeofenceID() {
        return geofenceID;
    }

    public String getRemindermessage() {
        return remindermessage;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }
}
