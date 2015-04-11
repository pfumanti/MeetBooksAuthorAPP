package com.example.android.meetbooksauthor.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.format.Time;

/**
 * Created by Administrator on 07/04/2015.
 */
public class MeetingContract {

    public static final String CONTENT_AUTHORITY = "com.example.android.meetbooksauthor";
    // Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
// the content provider.
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    // Possible paths (appended to base content URI for possible URI's)
// For instance, content://com.example.android.sunshine.app/weather/ is a valid path for
// looking at weather data. content://com.example.android.sunshine.app/givemeroot/ will fail,
// as the ContentProvider hasn't been given any information on what to do with "givemeroot".
// At least, let's hope not. Don't be that dev, reader. Don't be that dev.
    public static final String PATH_MEETING = "meeting";


    // To make it easy to query for the exact date, we normalize all dates that go into
    // the database to the start of the the Julian day at UTC.
    public static long normalizeDate(long startDate) {
        // normalize the start date to the beginning of the (UTC) day
        Time time = new Time();
        time.set(startDate);
        int julianDay = Time.getJulianDay(startDate, time.gmtoff);
        return time.setJulianDay(julianDay);
    }

    /*
        Inner class that defines the table contents of the location table
        Students: This is where you will add the strings.  (Similar to what has been
        done for WeatherEntry)
     */


    /* Inner class that defines the table contents of the meeting table */
    public static final class MeetingEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MEETING).build();
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MEETING;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MEETING;

        public static final String TABLE_NAME = "meeting";

        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_EVENT_ID = "event_id";
        // State id as returned by API, to identify the icon to be used
        public static final String COLUMN_STATE_ID = "state_id";

        // Short description and long description of the weather, as provided by API.
        // e.g "clear" vs "sky is clear".
        public static final String COLUMN_AUTHOR_NAME = "author_name";
        public static final String COLUMN_BOOK_TITLE = "book_title";
        // Date in format YYYYMMDD as integer
        public static final String COLUMN_END_DATE = "end_date";
        //Date in text format as returned by JSON
        public static final String COLUMN_TOUR_START_TIME = "tour_start_time";
        public static final String COLUMN_TOUR_END_TIME = "tour_end_time";
        public static final String COLUMN_TIMEOFDAY = "timeofday";
        public static final String COLUMN_BOOKING_DATE = "booking_date";
        public static final String COLUMN_VENUE = "venue";
        public static final String COLUMN_ADDRESS = "address";
        public static final String COLUMN_CITY = "city";

        public static Uri buildMeetingUri() {
            //return ContentUris.withAppendedId(CONTENT_URI, id);
            return CONTENT_URI;
        }

        public static Uri buildMeetingLocation(String stateSetting) {
            return CONTENT_URI.buildUpon().appendPath(stateSetting).build();
         }

        public static Uri buildMeetingLocationWithDate(String stateSetting, long date) {
            return CONTENT_URI.buildUpon().appendPath(stateSetting)
                .appendPath(Long.toString(normalizeDate(date))).build();
        }

        public static String getStateSettingFromUri(Uri uri) {
              return uri.getQueryParameter(COLUMN_STATE_ID);
        }


        public static int getTourEndDateFromUri(Uri uri) {
            return Integer.parseInt(uri.getQueryParameter(COLUMN_END_DATE));
           // return Integer.parseInt(app.substring(0,3)+app.substring(5,6)+app.substring(8,9));
        }

}


}
