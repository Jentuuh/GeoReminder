package com.example.georeminder;
import androidx.annotation.NonNull;
import androidx.room.*;


/**
 * @author jentevandersanden
 * Class that represents the row ReminderEntity in the table Reminders */
@Entity(tableName = "reminder")
public class ReminderEntity{

    /** Columns */
    @PrimaryKey @NonNull
    public String ID;

    @ColumnInfo(name = "message")
    private String message;

    @ColumnInfo(name = "longitude")
    private double longitude;

    @ColumnInfo(name = "latitude")
    private double latitude;


    /** GETTERS AND SETTERS*/
    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}
