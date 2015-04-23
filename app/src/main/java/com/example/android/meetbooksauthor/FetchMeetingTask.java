package com.example.android.meetbooksauthor;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.text.format.Time;
import android.util.Log;

import com.example.android.meetbooksauthor.data.MeetingContract.MeetingEntry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Vector;

/**
 * Created by Administrator on 07/04/2015.
 */
//first params is input type to doInBackground --> we pass days and state in OptionsItemSelected
//last param is the type of returned value
    //Deleted after intoducing MeetingAdapter
//public class FetchMeetingTask extends AsyncTask<String, Void, String[]> {

   public class FetchMeetingTask extends AsyncTask<String, Void, Void> {

    private final String LOG_TAG = FetchMeetingTask.class.getSimpleName();

    //private ArrayAdapter<String> mMeetingAdapter;

    private final Context mContext;

     public FetchMeetingTask(Context context) {
        mContext = context;
    }


    //private String[] getDataFromJson(String meetingJsonStr, int numDays)
    private void getDataFromJson(String meetingJsonStr, int numDays)
            throws JSONException {
// These are the names of the JSON objects that need to be extracted.
        final String OWM_LIST = "AA_EVENT";
        final String OWM_NAME = "NAME";
        final String OWM_TITLE = "TITLE";
        final String OWM_STATE = "STATE";
        final String OWM_TOUR_START_TIME = "TOUR_START_TIME";
        final String OWM_TOUR_END_TIME = "TOUR_END_TIME";
        final String OWM_TIMEOFDAY = "TIMEOFDAY";
        final String OWM_BOOKING_DATE = "BOOKING_DATE";
        final String OWM_VENUE = "VENUE";
        final String OWM_ADDRESS = "ADDRESS1";
        final String OWM_CITY = "CITY";
        final String OWM_EVENT = "EVENT_ID";

        try {

        JSONObject meetingJson = new JSONObject(meetingJsonStr);
        JSONArray meetingArray = meetingJson.getJSONArray(OWM_LIST);
// OWM returns daily forecasts based upon the local time of the city that is being
// asked for, which means that we need to know the GMT offset to translate this data
// properly.

        // Insert the new weather information into the database
        Vector<ContentValues> cVVector = new Vector<ContentValues>(meetingArray.length());

// Since this data is also sent in-order and the first day is always the
// current day, we're going to take advantage of that to get a nice
// normalized UTC date for all of our weather.
        Time dayTime = new Time();
        dayTime.setToNow();
// we start at the day returned by local time. Otherwise this is a mess.
        int julianStartDay = Time.getJulianDay(System.currentTimeMillis(), dayTime.gmtoff);
// now we work exclusively in UTC
        dayTime = new Time();

        String[] resultStrs = new String[numDays];
        //clear
        for(int i = 0; i < numDays; i++) {
            resultStrs[i] = "";
        }
        String state = "";
        for(int i = 0; i < meetingArray.length(); i++) {
// For now, using the format "Day, description, hi/low"

// Get the JSON object representing the day
            JSONObject dayForecast = meetingArray.getJSONObject(i);

            long  dateTime = dayTime.setJulianDay(julianStartDay+i);

            state = dayForecast.getString(OWM_STATE);
            String title = dayForecast.getString(OWM_TITLE);
            String name = dayForecast.getString(OWM_NAME);
            String startDay = dayForecast.getString(OWM_TOUR_START_TIME);
            String endDay = dayForecast.getString(OWM_TOUR_END_TIME);
            String timeOfDay = dayForecast.getString(OWM_TIMEOFDAY);
            String bookingDate = dayForecast.getString(OWM_BOOKING_DATE);
            String venue = dayForecast.getString(OWM_VENUE);
            String address = dayForecast.getString(OWM_ADDRESS);
            String city = dayForecast.getString(OWM_CITY);
            String eventId = dayForecast.getString(OWM_EVENT);

            ContentValues meetingValues = new ContentValues();
            meetingValues.put(MeetingEntry.COLUMN_STATE_ID, state);
            meetingValues.put(MeetingEntry.COLUMN_AUTHOR_NAME, name);
            meetingValues.put(MeetingEntry.COLUMN_BOOK_TITLE, title);
            meetingValues.put(MeetingEntry.COLUMN_END_DATE, endDay.substring(0,3)+endDay.substring(5,6)+endDay.substring(8,9));
            meetingValues.put(MeetingEntry.COLUMN_TOUR_START_TIME, startDay);
            meetingValues.put(MeetingEntry.COLUMN_TOUR_END_TIME, endDay);
            meetingValues.put(MeetingEntry.COLUMN_TIMEOFDAY, timeOfDay);
            meetingValues.put(MeetingEntry.COLUMN_BOOKING_DATE, bookingDate);
            meetingValues.put(MeetingEntry.COLUMN_VENUE, venue);
            meetingValues.put(MeetingEntry.COLUMN_ADDRESS, address);
            meetingValues.put(MeetingEntry.COLUMN_EVENT_ID, eventId);
            meetingValues.put(MeetingEntry.COLUMN_CITY, city);

            cVVector.add(meetingValues);


        }

      /*  for (String s : resultStrs) {
            Log.v(LOG_TAG, "Meeting entry: " + s);
        }*/

       // return resultStrs;

        int inserted = 0;
        //TMP add to database stop now

        if ( cVVector.size() > 0 ) {
            ContentValues[] cvArray = new ContentValues[cVVector.size()];
            cVVector.toArray(cvArray);
            inserted = mContext.getContentResolver().bulkInsert(MeetingEntry.CONTENT_URI, cvArray);
        }


        Log.d(LOG_TAG, "####FetchMeetingTask Complete. " + inserted + " Inserted");

    } catch (JSONException e) {
        Log.e(LOG_TAG, e.getMessage(), e);
        e.printStackTrace();
    }



    }

    @Override
   // protected String[] doInBackground(String... params) {
   protected Void doInBackground(String... params) {

        if (params.length == 0) {
            return null;
        }

        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        // Will contain the raw JSON response as a string.
        String meetingJsonStr = null;

        String apiName = "AuthorTour";
        String format = "JSON";
        String apiKey = "pts7zzjkhkvy6m72jqzbpnau";

        try {
            // Construct the URL for the OpenWeatherMap query
            // Possible parameters are avaiable at OWM's forecast API page, at
            // http://openweathermap.org/API#forecast
            // URL url = new URL("http://api.openweathermap.org/data/2.5/forecast/daily?q=94043&mode=json&units=metric&cnt=7");
            URL url_1 = new URL("http://api.harpercollins.com/api/v2/hcapim?apiname=AuthorTour&format=JSON&param1=4&param2=MA&apikey=pts7zzjkhkvy6m72jqzbpnau");
            final String MEET_BOOKS_AUTHORS_BASE_URL = "http://api.harpercollins.com/api/v2/hcapim?";

            final String API_NAME = "apiname";
            final String FORMAT_PARAM = "format";
            final String DAYS_PARAM = "param1";
            final String STATE_PARAM = "param2";
            final String API_KEY_PARAM = "apikey";


            if (params[1].equalsIgnoreCase("ANY")) {
                params[1]="";
            }
            Uri builtUri = Uri.parse(MEET_BOOKS_AUTHORS_BASE_URL).buildUpon()
                    .appendQueryParameter(API_NAME, apiName)
                    .appendQueryParameter(FORMAT_PARAM, format)
                    .appendQueryParameter(DAYS_PARAM, params[0])
                    .appendQueryParameter(STATE_PARAM, params[1])
                    .appendQueryParameter(API_KEY_PARAM, apiKey)
                    .build();
            URL url = new URL(builtUri.toString());

            // URL url = new URL(builtUri.toString());
             Log.v(LOG_TAG, "####Built URI " + builtUri.toString());

            // Create the request to harpercollins, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
// Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
// But it does make debugging a *lot* easier if you print out the completed
// buffer for debugging.
                buffer.append(line + "\n");
            }
            if (buffer.length() == 0) {
// Stream was empty. No point in parsing.
                return null;
            }
            meetingJsonStr = buffer.toString();
            //added
            getDataFromJson(meetingJsonStr,70);


        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
// If the code didn't successfully get the weather data, there's no point in attempting
// to parse it.
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }
        return null;

    }


}


