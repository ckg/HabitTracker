package com.example.android.habittracker;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.android.habittracker.data.HabitDbHelper;
import com.example.android.habittracker.data.HabitContract.HabitEntry;


/**
 * Displays list of habits that were entered and stored in the app.
 */
public class HabitTrackerActivity extends AppCompatActivity {

    public static final String LOG_TAG = HabitTrackerActivity.class.getName();

    /** Database helper that will provide us access to the database */
    private HabitDbHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_tracker);

        // Setup FAB to open EditorActivity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HabitTrackerActivity.this, HabitEditorActivity.class);
                startActivity(intent);
            }
        });

        // To access our database, we instantiate our subclass of SQLiteOpenHelper
        // and pass the context, which is the current activity.
        mDbHelper = new HabitDbHelper(this);

    }

    @Override
    protected void onStart(){
        super.onStart();
        displayDatabaseInfo();
    }
    private Cursor readCursor() {
        // Create and/or open a database to read from it
        SQLiteDatabase db = mDbHelper.getReadableDatabase();


        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                HabitEntry._ID,
                HabitEntry.COLUMN_HABIT_NAME,
                HabitEntry.COLUMN_HABIT_TRACKING_START_DATE,
                HabitEntry.COLUMN_HABIT_TRACKING_STOP_DATE,
                HabitEntry.COLUMN_HABIT_TARGET_FREQUENCY,
                HabitEntry.COLUMN_HABIT_TARGET_FREQUENCY_ACHIEVED
        };

        // Perform a query on the habits table
        Cursor cursor = db.query(
                HabitEntry.TABLE_NAME,   // The table to query
                projection,            // The columns to return
                null,                  // The columns for the WHERE clause
                null,                  // The values for the WHERE clause
                null,                  // Don't group the rows
                null,                  // Don't filter by row groups
                null);                  // The sort order
        return cursor;
    }

    /**
     * Temporary helper method to display information in the onscreen TextView about the state of
     * the pets database.
     */
    private void displayDatabaseInfo() {

        Cursor cursor = readCursor();

        TextView displayView = (TextView) findViewById(R.id.text_view_habit);

        try {

            // Create a header in the Text View that looks like this:
            //
            // The habits table contains <number of rows in Cursor> habits.
            // _id - name - start_date - stop_date - target -achieved
            //
            // In the while loop below, iterate through the rows of the cursor and display
            // the information from each column in this order.
            displayView.setText("The habits table contains " + cursor.getCount() + " habits.\n\n");
            displayView.append(HabitEntry._ID + " - " +
                    HabitEntry.COLUMN_HABIT_NAME + " - " +
                    HabitEntry.COLUMN_HABIT_TRACKING_START_DATE+ " - " +
                    HabitEntry.COLUMN_HABIT_TRACKING_STOP_DATE + " - " +
                    HabitEntry.COLUMN_HABIT_TARGET_FREQUENCY + " - " +
                    HabitEntry.COLUMN_HABIT_TARGET_FREQUENCY_ACHIEVED + "\n");

            // Figure out the index of each column
            int idColumnIndex = cursor.getColumnIndex(HabitEntry._ID);
            int nameColumnIndex = cursor.getColumnIndex(HabitEntry.COLUMN_HABIT_NAME);
            int startColumnIndex = cursor.getColumnIndex(HabitEntry.COLUMN_HABIT_TRACKING_START_DATE);
            int stopColumnIndex = cursor.getColumnIndex(HabitEntry.COLUMN_HABIT_TRACKING_STOP_DATE);
            int targetColumnIndex = cursor.getColumnIndex(HabitEntry.COLUMN_HABIT_TARGET_FREQUENCY);
            int achievedColumnIndex = cursor.getColumnIndex(HabitEntry.COLUMN_HABIT_TARGET_FREQUENCY_ACHIEVED);

            while (cursor.moveToNext()) {
                // Use that index to extract the String or Int value of the word
                // at the current row the cursor is on.
                int currentID = cursor.getInt(idColumnIndex);
                String currentName = cursor.getString(nameColumnIndex);
                String currentStart = cursor.getString(startColumnIndex);
                String currentStop = cursor.getString(stopColumnIndex);
                int currentTarget = cursor.getInt(targetColumnIndex);
                int currentAchieved = cursor.getInt(achievedColumnIndex);
                // Display the values from each column of the current row in the cursor in the TextView
                displayView.append(("\n" + currentID + " - " +
                        currentName + " - " +
                        currentStart + " - " +
                        currentStop + " - " +
                        currentTarget + " - " +
                        currentAchieved));
            }

        } finally {
            // Always close the cursor when you're done reading from it. This releases all its
            // resources and makes it invalid.
            cursor.close();
        }
    }

    private void insertHabit(){
        // Gets the database in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Create a ContentValues object where column names are the keys,
        // and reading's habit attributes are the values.
        ContentValues values = new ContentValues();
        values.put(HabitEntry.COLUMN_HABIT_NAME, "reading");
        values.put(HabitEntry.COLUMN_HABIT_TRACKING_START_DATE, "1-1-17");
        values.put(HabitEntry.COLUMN_HABIT_TRACKING_STOP_DATE, "1-2-17");
        values.put(HabitEntry.COLUMN_HABIT_TARGET_FREQUENCY, HabitEntry.TARGET_FREQUENCY_ONCE_A_WEEK);
        values.put(HabitEntry.COLUMN_HABIT_TARGET_FREQUENCY_ACHIEVED, HabitEntry.TARGET_FREQUENCY_ACHIEVED_YES);

        // Insert a new row for reading in the database, returning the ID of that new row.
        // The first argument for db.insert() is the habits table name.
        // The second argument provides the name of a column in which the framework
        // can insert NULL in the event that the ContentValues is empty (if
        // this is set to "null", then the framework will not insert a row when
        // there are no values).
        // The third argument is the ContentValues object containing the info for reading.
        long newRowId = db.insert(HabitEntry.TABLE_NAME, null, values);

        Log.v(LOG_TAG,"New row ID"+ newRowId);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_tracker, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_insert_dummy_data:
                // Insert dummy_habit
                insertHabit();
                displayDatabaseInfo();
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                // Do nothing for now
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

