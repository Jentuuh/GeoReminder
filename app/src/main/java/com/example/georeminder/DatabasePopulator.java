package com.example.georeminder;

/**
 * @author jentevandersanden
 * This class can be used to add reminders to the database instance
 * or
 * delete them from the database instance
 */
public class DatabasePopulator{

    /**
     * Adds a new reminder to the database
     * @param db : The database instance
     * @param reminder : The reminder to be inserted
     */
    public static void addReminder(AppDatabase db, ReminderEntity reminder){
        db.reminderDAO().insertReminder(reminder);
    }

    /**
     * Deletes a new reminder from the database
     * @param db : The database instance
     * @param reminder : The reminder to be inserted
     */
    public static void deleteReminder(AppDatabase db, ReminderEntity reminder){
        db.reminderDAO().deleteReminder(reminder);
    }


    /**
     * Adds a test reminder to the database instance
     * @param db : The database instance
     */
    public static void addTestReminder(AppDatabase db){
            ReminderEntity reminder = new ReminderEntity();
            reminder.setID("003");
            reminder.setLongitude(5.391212);
            reminder.setLatitude(50.925665);
            reminder.setRadius(500f);
            reminder.setMessage("You've just entered the area!");
            addReminder(db, reminder);
    }
}
