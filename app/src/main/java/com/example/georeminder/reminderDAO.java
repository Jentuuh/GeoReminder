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
    @Query("SELECT * FROM reminder")
    List<ReminderEntity> getAll();

    @Insert
    void insertReminder(ReminderEntity reminder);

    @Delete
    void deleteReminder (ReminderEntity reminder);

}
