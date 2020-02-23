package com.example.georeminder;
import android.content.Context;

import androidx.room.*;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;


/**
 * @author jentevandersanden
 * Abstract class that represents the database holder for the database
 */
@Database(entities = {ReminderEntity.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {

    // Singleton instance of the database
    private static AppDatabase INSTANCE;
    public abstract reminderDAO reminderDAO();

    public static AppDatabase getReminderDatabase(Context context){
        if (INSTANCE == null) {
            INSTANCE =
                    Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "Reminder-Database").allowMainThreadQueries()
                            .addMigrations(MIGRATION_1_2).build();
        }
        return INSTANCE;
    }

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE reminder ADD COLUMN radius FLOAT NOT NULL DEFAULT 200");
        }
    };

    /**
     * Destroys the singleton instance of the database.
     */
    public static void destroyInstance(){
        INSTANCE = null;
    }

}





