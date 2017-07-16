package com.example.android.habittracker.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.habittracker.data.HabitContract.HabitEntry;

/**
 * Created by Christos on 02-Jul-17.
 */

public class HabitDbHelper extends SQLiteOpenHelper {


    public static final String LOG_TAG = HabitDbHelper.class.getSimpleName();

    /** Name of the database file */
    private static final String DATABASE_NAME = "habits_tracker.db";

    /**
     * Database version. If you change the database schema, you must increment the database version.
     */
    private static final int DATABASE_VERSION = 1;

    /**
     * Constructs a new instance of {@link HabitDbHelper}.
     *
     * @param context of the app
     */

    public HabitDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Create a String that contains the SQL statement to create the Habits table
        String SQL_CREATE_HABITS_TABLE =  "CREATE TABLE " + HabitEntry.TABLE_NAME + " ("
                +HabitEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                +HabitEntry.COLUMN_HABIT_NAME + " TEXT NOT NULL, "
                +HabitEntry.COLUMN_HABIT_TRACKING_START_DATE + " TEXT, "
                +HabitEntry.COLUMN_HABIT_TRACKING_STOP_DATE + " TEXT, "
                +HabitEntry.COLUMN_HABIT_TARGET_FREQUENCY + " INTEGER NOT NULL DEFAULT 0, "
                +HabitEntry.COLUMN_HABIT_TARGET_FREQUENCY_ACHIEVED + " INTEGER NOT NULL DEFAULT 0);";
        // Execute the SQL statement
        db.execSQL(SQL_CREATE_HABITS_TABLE);
    }

    /**
     * This is called when the database needs to be upgraded.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // The database is still at version 1, so there's nothing to do be done here.
    }
}
