package com.example.android.habittracker;

/**
 * Created by Christos on 02-Jul-17.
 */

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.android.habittracker.data.HabitContract.HabitEntry;
import com.example.android.habittracker.data.HabitDbHelper;

/**
 * Allows user to create a new pet or edit an existing one.
 */
public class HabitEditorActivity extends AppCompatActivity {

    /** EditText field to enter the habit's name */
    private EditText mNameEditText;

    /** EditText field to enter the start date */
    private EditText mStartEditText;

    /** EditText field to enter the stop date */
    private EditText mStopEditText;

    /** EditText field to enter the habit's target frequency */
    private Spinner mTargetSpinner;

    /**
     * Target frequency of the habit. The possible values are:
     * 0 for once a week, 1 for twice a week, 2 for everyday.
     */
    private int mTarget = 0;

    /** EditText field to enter the habit's achieved frequency */
    private Spinner mAchievedSpinner;

    /**
     * Achieved frequency of the habit. The possible values are:
     * 0 for not achieved, 1 for achieved.
     */
    private int mAchieved = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_editor);

        // Find all relevant views that we will need to read user input from
        mNameEditText = (EditText) findViewById(R.id.edit_habit_name);
        mStartEditText = (EditText) findViewById(R.id.edit_habit_start);
        mStopEditText = (EditText) findViewById(R.id.edit_habit_stop);
        mTargetSpinner = (Spinner) findViewById(R.id.spinner_target);
        mAchievedSpinner = (Spinner) findViewById(R.id.spinner_achieved);

        setupTargetSpinner();
        setupAchievedSpinner();
    }

    /**
     * Setup the dropdown spinner that allows the user to select the target frequency of the habit.
     */
    private void setupTargetSpinner() {
        // Create adapter for spinner. The list options are from the String array it will use
        // the spinner will use the default layout
        ArrayAdapter targetSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_target_frequency_options, android.R.layout.simple_spinner_item);

        // Specify dropdown layout style - simple list view with 1 item per line
        targetSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Apply the adapter to the spinner
        mTargetSpinner.setAdapter(targetSpinnerAdapter);

        // Set the integer mSelected to the constant values
        mTargetSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.target_frequency_once_a_week))) {
                        mTarget = HabitEntry.TARGET_FREQUENCY_ONCE_A_WEEK; // Once a week
                    } else if (selection.equals(getString(R.string.target_frequency_twice_a_week))) {
                        mTarget = HabitEntry.TARGET_FREQUENCY_TWICE_A_WEEK; // Twice a week
                    } else {
                        mTarget = HabitEntry.TARGET_FREQUENCY_EVERY_DAY; // Every day
                    }
                }
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mTarget = 0; // Once a week
            }
        });
    }

    /**
     * Setup the dropdown spinner that allows the user to select the achieved frequency of the habit.
     */
    private void setupAchievedSpinner() {
        // Create adapter for spinner. The list options are from the String array it will use
        // the spinner will use the default layout
        ArrayAdapter achievedSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_target_frequency_achieved_options, android.R.layout.simple_spinner_item);

        // Specify dropdown layout style - simple list view with 1 item per line
        achievedSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Apply the adapter to the spinner
        mAchievedSpinner.setAdapter(achievedSpinnerAdapter);

        // Set the integer mSelected to the constant values
        mAchievedSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.target_frequency_achieved_no))) {
                        mAchieved = HabitEntry.TARGET_FREQUENCY_ACHIEVED_NO; //
                    } else {
                        mAchieved = HabitEntry.TARGET_FREQUENCY_ACHIEVED_YES; //
                    }
                }
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mAchieved = 0; // Not Achieved
            }
        });
    }


    private void insertHabit(){

        // Read from input fields
        // Use trim to eliminate leading or trailing white space
        String nameString = mNameEditText.getText().toString().trim();
        String startString = mStartEditText.getText().toString().trim();
        String stopString = mStopEditText.getText().toString().trim();

        // Create database helper
        HabitDbHelper mDbHelper = new HabitDbHelper(this);

        // Gets the database in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Create a ContentValues object where column names are the keys,
        // and pet attributes from the editor are the values.
        ContentValues values = new ContentValues();
        values.put(HabitEntry.COLUMN_HABIT_NAME, nameString);
        values.put(HabitEntry.COLUMN_HABIT_TRACKING_START_DATE, startString);
        values.put(HabitEntry.COLUMN_HABIT_TRACKING_STOP_DATE, stopString);
        values.put(HabitEntry.COLUMN_HABIT_TARGET_FREQUENCY, mTarget);
        values.put(HabitEntry.COLUMN_HABIT_TARGET_FREQUENCY_ACHIEVED, mAchieved);

        // Insert a new row for pet in the database, returning the ID of that new row.
        long newRowId = db.insert(HabitEntry.TABLE_NAME, null, values);

        // Show a toast message depending on whether or not the insertion was successful
        if (newRowId == -1) {
            // If the row ID is -1, then there was an error with insertion.
            Toast.makeText(this, "Error with saving habit", Toast.LENGTH_SHORT).show();
        } else {
            // Otherwise, the insertion was successful and we can display a toast with the row ID.
            Toast.makeText(this, "Habit saved with row id: " + newRowId, Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_save:
                // Add habit to database
                insertHabit();
                //Exit the activity and return to the previous activity
                finish();
                return true;
            // Respond to a click on the "Delete" menu option
            case R.id.action_delete:
                // Do nothing for now
                return true;
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                // Navigate back to parent activity (CatalogActivity)
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}