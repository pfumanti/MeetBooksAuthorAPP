package com.example.android.meetbooksauthor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.android.meetbooksauthor.data.MeetingContract;

/**
 * Created by Administrator on 04/04/2015.
 */
//public class MeetingFragment extends Fragment{
//define our callbacks
public class MeetingFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    //use a different id for each cursor in our activity
    public static final int MEETING_CURSOR_LOADER = 0;

    // Specify the columns we need.
    private static final String[] MEETING_COLUMNS = {
// In this case the id needs to be fully qualified with a table name, since
// the content provider joins the location & weather tables in the background
// (both have an _id column)
// On the one hand, that's annoying. On the other, you can search the weather table
// using the location set by the user, which is only in the Location table.
// So the convenience is worth it.
            MeetingContract.MeetingEntry.COLUMN_ID,
            MeetingContract.MeetingEntry.COLUMN_EVENT_ID,
            MeetingContract.MeetingEntry.COLUMN_STATE_ID,
            MeetingContract.MeetingEntry.COLUMN_AUTHOR_NAME,
            MeetingContract.MeetingEntry.COLUMN_BOOK_TITLE,
            MeetingContract.MeetingEntry.COLUMN_TOUR_START_TIME,
            MeetingContract.MeetingEntry.COLUMN_TOUR_END_TIME,
            MeetingContract.MeetingEntry.COLUMN_TIMEOFDAY,
            MeetingContract.MeetingEntry.COLUMN_END_DATE,
            MeetingContract.MeetingEntry.COLUMN_VENUE,
            MeetingContract.MeetingEntry.COLUMN_ADDRESS,
            MeetingContract.MeetingEntry.COLUMN_BOOKING_DATE,
            MeetingContract.MeetingEntry.COLUMN_CITY

    };


    static final int COL_ID = 0;
    static final int COL_EVENT_ID = 1;
    static final int COL_STATE_ID = 2;
    static final int COL_AUTHOR_NAME = 3;
    static final int COL_BOOK_TITLE = 4;
    static final int COL_TOUR_START_TIME = 5;
    static final int COL_TOUR_END_TIME = 6;
    static final int COL_TIMEOFDAY = 7;
    static final int COL_END_DATE = 8;
    static final int COL_VENUE = 9;
    static final int COL_ADDRESS = 10;
    static final int COL_BOOKING_DATE = 11;
    static final int COL_CITY = 12;

    private MeetingAdapter mMeetingAdapter;

    public MeetingFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
// Add this line in order for this fragment to handle menu events.
        setHasOptionsMenu(true);
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.meetingfragment, menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
// Handle action bar item clicks here. The action bar will
// automatically handle clicks on the Home/Up button, so long
// as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            updateMeetingList();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // The CursorAdapter will take data from our cursor and populate the ListView.
        mMeetingAdapter = new MeetingAdapter(getActivity(), null, 0);

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        // Get a reference to the ListView, and attach this adapter to it.
        ListView listView = (ListView) rootView.findViewById(R.id.listview_meeting);
        listView.setAdapter(mMeetingAdapter);

        // We'll call our MainActivity
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
// CursorAdapter returns a cursor at the correct position for getItem(), or null
// if it cannot seek to that position.
                Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);
                if (cursor != null) {

                    Intent intent = new Intent(getActivity(), MeetingDetailActivity.class)
                            .setData(MeetingContract.MeetingEntry.buildMeetingUri());
                    startActivity(intent);
                }
            }
        });


        return rootView;


   }

   @Override
   public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(MEETING_CURSOR_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
   }


   private void updateMeetingList() {
        //FetchMeetingTask meetingTask = new FetchMeetingTask(getActivity(), mMeetingAdapter);
        FetchMeetingTask meetingTask = new FetchMeetingTask(getActivity());
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        //In no value is stored in the pair <key-value>, then get default value
        String remaining_days = prefs.getString(getString(R.string.pref_remaining_days_key), getString(R.string.pref_remaining_days_default));
        String state_location = prefs.getString(getString(R.string.pref_states_key),getString(R.string.pref_states_ma));
        meetingTask.execute(remaining_days, state_location);

    }

    void onLocationChanged( ) {
        updateMeetingList();
        getLoaderManager().restartLoader(MEETING_CURSOR_LOADER, null, this);
    }

   // @Override
    //When fragment starts ... call updateMeeting
    /*
    public void onStart() {
        super.onStart();
        updateMeetingList();
    }
    */

        @Override
   public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
           // String sortOrder = WeatherContract.WeatherEntry.COLUMN_DATE + " ASC";

            Uri uri = MeetingContract.MeetingEntry.buildMeetingUri();

            return new CursorLoader(getActivity(),
                    uri,
                    null,//MEETING_COLUMNS, //null
                    null,
                    null,
                    null);
        }
        @Override
        public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
            mMeetingAdapter.swapCursor(cursor);
        }
        @Override
        public void onLoaderReset(Loader<Cursor> cursorLoader) {
            mMeetingAdapter.swapCursor(null);
        }


}
