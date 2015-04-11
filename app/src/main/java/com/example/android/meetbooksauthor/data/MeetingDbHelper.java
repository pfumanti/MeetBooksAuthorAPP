package com.example.android.meetbooksauthor.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.meetbooksauthor.data.MeetingContract.MeetingEntry;

/**
 * Created by Administrator on 07/04/2015.
 */

public class MeetingDbHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 8;

    static final String DATABASE_NAME = "meeting.db";

    public MeetingDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_MEETING_TABLE = "CREATE TABLE " + MeetingEntry.TABLE_NAME + " (" +
                // Why AutoIncrement here, and not above?
                // Unique keys will be auto-generated in either case.  But for weather
                // forecasting, it's reasonable to assume the user will want information
                // for a certain date and all dates *following*, so the forecast data
                // should be sorted accordingly.
                MeetingEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT , " +
                MeetingEntry.COLUMN_EVENT_ID + " TEXT NOT NULL UNIQUE," +
                MeetingEntry.COLUMN_STATE_ID + " TEXT NOT NULL," +
                MeetingEntry.COLUMN_AUTHOR_NAME + " TEXT NOT NULL, " +
                MeetingEntry.COLUMN_BOOK_TITLE + " TEXT NOT NULL, " +
                MeetingEntry.COLUMN_END_DATE + " TEXT NOT NULL, " +
                MeetingEntry.COLUMN_TOUR_START_TIME + " TEXT NOT NULL, " +
                MeetingEntry.COLUMN_TOUR_END_TIME + " TEXT NOT NULL, " +
                MeetingEntry.COLUMN_TIMEOFDAY + " TEXT NOT NULL, " +
                MeetingEntry.COLUMN_BOOKING_DATE + " TEXT NOT NULL, " +
                MeetingEntry.COLUMN_VENUE + " TEXT NOT NULL, " +
                MeetingEntry.COLUMN_ADDRESS + " TEXT NOT NULL, " +
                MeetingEntry.COLUMN_CITY + " TEXT NOT NULL); ";


                sqLiteDatabase.execSQL(SQL_CREATE_MEETING_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

        // Note that this only fires if you change the version number for your database.
        //  see DATABASE_VERSION constant defined earlier
        // If you want to update the schema without wiping data, commenting out the next line
        // should be your top priority before modifying this method.

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MeetingEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

}
