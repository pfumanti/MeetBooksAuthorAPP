package com.example.android.meetbooksauthor.data;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

/**
 * Created by Administrator on 07/04/2015.

public class MeetingProvider {

}
*/

public class MeetingProvider extends ContentProvider {
    // The URI Matcher used by this content provider.
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private MeetingDbHelper mOpenHelper;
    static final int MEETING = 100;
    static final int MEETING_WITH_STATE = 101;
    static final int MEETING_WITH_STATE_AND_DATE = 102;

    private static final SQLiteQueryBuilder sMeetingByStateSettingQueryBuilder;

    static{
        sMeetingByStateSettingQueryBuilder = new SQLiteQueryBuilder();
        sMeetingByStateSettingQueryBuilder.setTables(MeetingContract.MeetingEntry.TABLE_NAME);
//This is an inner join which looks like
//weather INNER JOIN location ON weather.location_id = location._id
        /*
        sMeetingByStateSettingQueryBuilder.setTables(
                MeetingContract.MeetingEntry.TABLE_NAME + " INNER JOIN " +
                        WeatherContract.LocationEntry.TABLE_NAME +
                        " ON " + WeatherContract.WeatherEntry.TABLE_NAME +
                        "." + WeatherContract.WeatherEntry.COLUMN_LOC_KEY +
                        " = " + WeatherContract.LocationEntry.TABLE_NAME +
                        "." + WeatherContract.LocationEntry._ID);
                        */
    }



    //location.location_setting = ?
    private static final String sStateSettingSelection =
            MeetingContract.MeetingEntry.TABLE_NAME+
                    "." + MeetingContract.MeetingEntry.COLUMN_STATE_ID + " = ? ";

    //location.location_setting = ? AND date >= ?
    private static final String sStateSettingWithTourEndDateSelection =
                    MeetingContract.MeetingEntry.COLUMN_STATE_ID + " = ? AND " +
                    MeetingContract.MeetingEntry.COLUMN_END_DATE + " <= ? ";

    private Cursor getMeetingByStateSetting(Uri uri, String[] projection, String sortOrder) {
        String stateSetting = MeetingContract.MeetingEntry.getStateSettingFromUri(uri);

        String[] selectionArgs;
        String selection;

        selection = sStateSettingSelection;
        selectionArgs = new String[]{stateSetting};

        return sMeetingByStateSettingQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }

    private Cursor getMeetingByStateSettingAndDate(
            Uri uri, String[] projection, String sortOrder) {
        String stateSetting = MeetingContract.MeetingEntry.getStateSettingFromUri(uri);
        int endDateSelection = MeetingContract.MeetingEntry.getTourEndDateFromUri(uri);

       // sStateSettingWithTourEndDateSelection

        String[] selectionArgs;
        String selection;

        selection = sStateSettingWithTourEndDateSelection;
        selectionArgs = new String[]{stateSetting,Integer.toString(endDateSelection)};

        return sMeetingByStateSettingQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }
    /*
    Students: Here is where you need to create the UriMatcher. This UriMatcher will
    match each URI to the WEATHER, WEATHER_WITH_LOCATION, WEATHER_WITH_LOCATION_AND_DATE,
    and LOCATION integer constants defined above. You can test this by uncommenting the
    testUriMatcher test within TestUriMatcher.
    */
    static UriMatcher buildUriMatcher() {
// I know what you're thinking. Why create a UriMatcher when you can use regular
// expressions instead? Because you're not crazy, that's why.
// All paths added to the UriMatcher have a corresponding code to return when a match is
// found. The code passed into the constructor represents the code to return for the root
// URI. It's common to use NO_MATCH as the code for this case.
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MeetingContract.CONTENT_AUTHORITY;
// For each type of URI you want to add, create a corresponding code.
        matcher.addURI(authority, MeetingContract.PATH_MEETING, MEETING);
        matcher.addURI(authority, MeetingContract.PATH_MEETING + "/*", MEETING_WITH_STATE);
        matcher.addURI(authority, MeetingContract.PATH_MEETING + "/*/#", MEETING_WITH_STATE_AND_DATE);
        return matcher;
    }
    /*
    Students: We've coded this for you. We just create a new WeatherDbHelper for later use
    here.
    */
    @Override
    public boolean onCreate() {
        mOpenHelper = new MeetingDbHelper(getContext());
        return true;
    }
    /*
    Students: Here's where you'll code the getType function that uses the UriMatcher. You can
    test this by uncommenting testGetType in TestProvider.
    */
    @Override
    public String getType(Uri uri) {
// Use the Uri Matcher to determine what kind of URI this is.
        final int match = sUriMatcher.match(uri);
        switch (match) {
// Student: Uncomment and fill out these two cases
            case MEETING_WITH_STATE_AND_DATE:
                return MeetingContract.MeetingEntry.CONTENT_TYPE;
            case MEETING_WITH_STATE:
                return MeetingContract.MeetingEntry.CONTENT_TYPE;
            case MEETING:
                return MeetingContract.MeetingEntry.CONTENT_TYPE;
             default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
// Here's the switch statement that, given a URI, will determine what kind of request it is,
// and query the database accordingly.
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
// "weather/*/*"
            case MEETING_WITH_STATE_AND_DATE:
            {
                retCursor = getMeetingByStateSettingAndDate(uri, projection, sortOrder);
                break;
            }
// "weather/*"
            case MEETING_WITH_STATE: {
                retCursor = getMeetingByStateSetting(uri, projection, sortOrder);
                break;
            }
// "weather"
            case MEETING: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MeetingContract.MeetingEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }


            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }
    /*
    Student: Add the ability to insert Locations to the implementation of this function.
    */
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;
        switch (match) {
            case MEETING: {
                long _id = db.insert(MeetingContract.MeetingEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = MeetingContract.MeetingEntry.buildMeetingUri();
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;
// this makes delete all rows return the number of rows deleted
        if ( null == selection ) selection = "1";
        switch (match) {
            case MEETING:
                rowsDeleted = db.delete(
                        MeetingContract.MeetingEntry.TABLE_NAME, selection, selectionArgs);
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
// Because a null deletes all rows
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(
            Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;
        switch (match) {
            case MEETING:
                  rowsUpdated = db.update(MeetingContract.MeetingEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    public Cursor fetchAll() {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        return db.query(MeetingContract.MeetingEntry.TABLE_NAME,
                new String[] { MeetingContract.MeetingEntry.COLUMN_STATE_ID,
                        MeetingContract.MeetingEntry.COLUMN_AUTHOR_NAME,
                        MeetingContract.MeetingEntry.COLUMN_BOOK_TITLE,
                        MeetingContract.MeetingEntry.COLUMN_END_DATE
                        },
                        null, null, null, null, null);
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case MEETING:
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(MeetingContract.MeetingEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            default:
                return super.bulkInsert(uri, values);
        }
    }
    // You do not need to call this method. This is a method specifically to assist the testing
// framework in running smoothly. You can read more at:
// http://developer.android.com/reference/android/content/ContentProvider.html#shutdown()
    @Override
    @TargetApi(11)
    public void shutdown() {
        mOpenHelper.close();
        super.shutdown();
    }
}
