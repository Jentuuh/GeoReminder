package com.example.georeminder;
import android.content.Context;

import androidx.room.*;


/**
 * @author jentevandersanden
 * Abstract class that represents the database holder for the database
 */
@Database(entities = {ReminderEntity.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    // Singleton instance of the database
    private static AppDatabase INSTANCE;
    public abstract reminderDAO reminderDAO();

    public static AppDatabase getReminderDatabase(Context context){
        if (INSTANCE == null) {
            INSTANCE =
                    Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "Reminder-Database").allowMainThreadQueries().build();
        }
        return INSTANCE;
    }

    /**
     * Destroys the singleton instance of the database.
     */
    public static void destroyInstance(){
        INSTANCE = null;
    }

}





