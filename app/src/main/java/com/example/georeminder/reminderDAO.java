package com.example.georeminder;
import androidx.room.*;

import java.util.List;

/**
 * @author jentevandersanden
 * This interface represents the functions that perform query's on the
 * Reminder Room database.
 */
@Dao
public interface reminderDAO {
    /**
     * Gets all reminders from the database
     * @return
     */
    @Query("SELECT * FROM reminder")
    List<ReminderEntity> getAll();

    /**
     * Inserts a new ReminderEntity into the database
     * @param reminder : reminder to be inserted
     */
    @Insert
    void insertReminder(ReminderEntity reminder);

    /**
     * Deletes a reminder from the database
     * @param reminder : reminder to be deleted.
     */
    @Delete
    void deleteReminder (ReminderEntity reminder);

}
