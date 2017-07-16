package com.example.android.habittracker.data;

import android.provider.BaseColumns;

/**
 * Created by Christos on 02-Jul-17.
 */

public final class HabitContract {

    public static final class HabitEntry implements BaseColumns {

        public static final String TABLE_NAME = "habits";

        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_HABIT_NAME= "name";
        public static final String COLUMN_HABIT_TRACKING_START_DATE= "start_date";
        public static final String COLUMN_HABIT_TRACKING_STOP_DATE= "stop_date";
        public static final String COLUMN_HABIT_TARGET_FREQUENCY= "frequency";
        public static final String COLUMN_HABIT_TARGET_FREQUENCY_ACHIEVED= "achieved";

        /**
         * Possible values for the "target frequency" of the HABIT.
         */
        public static final int TARGET_FREQUENCY_ONCE_A_WEEK = 0;
        public static final int TARGET_FREQUENCY_TWICE_A_WEEK= 1;
        public static final int TARGET_FREQUENCY_EVERY_DAY= 2;


        /**
         * Possible values for the "achieved frequency " of the HABIT.
         */
        public static final int TARGET_FREQUENCY_ACHIEVED_NO = 0;
        public static final int TARGET_FREQUENCY_ACHIEVED_YES= 1;


    }

}
