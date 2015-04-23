package com.example.android.meetbooksauthor;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
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

    private MeetingAdapter mMeetingAdapter;
    private ListView mListView;
    private int mPosition = ListView.INVALID_POSITION;
    private static final String SELECTED_KEY = "selected_position";

    //use a different id for each cursor in our activity
    public static final int MEETING_CURSOR_LOADER = 0;


    private final String LOG_TAG = MeetingFragment.class.getSimpleName();


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

   // private MeetingAdapter mMeetingAdapter;

    public MeetingFragment() {
    }

    /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of item
     * selections.
     */
    public interface Callback {
        /**
         * DetailFragmentCallback for when an item has been selected.
         */
        public void onItemSelected(Uri dateUri);
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

        // TheMeetingAdapter will take data from a source and
        // use it to populate the ListView it's attached to.
        mMeetingAdapter = new MeetingAdapter(getActivity(), null, 0);

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        // Get a reference to the ListView, and attach this adapter to it.
        mListView = (ListView) rootView.findViewById(R.id.listview_meeting);
        mListView.setAdapter(mMeetingAdapter);

        // We'll call our MainActivity
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {


// CursorAdapter returns a cursor at the correct position for getItem(), or null
// if it cannot seek to that position.
                Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);
                if (cursor != null) {

                    String locationSetting = Uty.getStateLocation(getActivity());
                    //Intent intent = new Intent(getActivity(), MeetingDetailActivity.class)
                    //        .setData(MeetingContract.MeetingEntry.buildMeetingLocation("MA")); //buildMeetingUri());
                    ((Callback) getActivity())
                             .onItemSelected(MeetingContract.MeetingEntry.buildMeetingLocation((locationSetting)));

                    String Nome =  cursor.getString(COL_AUTHOR_NAME);
                    Log.d(LOG_TAG, "############# " + Nome);
                    /*
                     String locationSetting = Utility.getPreferredLocation(getActivity());
                    Intent intent = new Intent(getActivity(), DetailActivity.class)
                                .setData(WeatherContract.WeatherEntry.buildWeatherLocationWithDate(
                                locationSetting, cursor.getLong(COL_WEATHER_DATE)
                        ));
                        startActivity(intent);

                     */


                   //tolta dalla callback startActivity(intent);
                }
                mPosition = position;
            }
        });

        // If there's instance state, mine it for useful information.
        // The end-goal here is that the user never knows that turning their device sideways
        // does crazy lifecycle related things. It should feel like some stuff stretched out,
        // or magically appeared to take advantage of room, but data or place in the app was never
        // a    ctually *lost*.
        if (savedInstanceState != null && savedInstanceState.containsKey(SELECTED_KEY)) {
            // The listview probably hasn't even been populated yet. Actually perform the
            // swapout in onLoadFinished.
            mPosition = savedInstanceState.getInt(SELECTED_KEY);
        }


        return rootView;


   }

   @Override
   public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(MEETING_CURSOR_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
   }


    void onLocationChanged( ) {
        updateMeetingList();
        getLoaderManager().restartLoader(MEETING_CURSOR_LOADER, null, this);
    }


   private void updateMeetingList() {
        //FetchMeetingTask meetingTask = new FetchMeetingTask(getActivity(), mMeetingAdapter);
        FetchMeetingTask meetingTask = new FetchMeetingTask(getActivity());
        String locationSetting = Uty.getStateLocation(getActivity());
        String remaining_days = Uty.getRemainingDays(getActivity());
       Log.d(LOG_TAG, "######## MeetingFragment:updateMeetingList:State=" + locationSetting + " RemDays=" + remaining_days);

        meetingTask.execute(remaining_days, locationSetting);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
// When tablets rotate, the currently selected list item needs to be saved.
// When no item is selected, mPosition will be set to Listview.INVALID_POSITION,
// so check for that before storing.
        if (mPosition != ListView.INVALID_POSITION) {
            outState.putInt(SELECTED_KEY, mPosition);
        }
        super.onSaveInstanceState(outState);
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

            String locationSetting = Uty.getStateLocation(getActivity());
            Uri uri = MeetingContract.MeetingEntry.buildMeetingLocation(locationSetting);

            return new CursorLoader(getActivity(),
                    uri,
                    MEETING_COLUMNS,
                    null,
                    null,
                    null);
        }

       // @Override
       // public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
       //     mMeetingAdapter.swapCursor(cursor);
       // }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            mMeetingAdapter.swapCursor(data);
            Log.d(LOG_TAG, "######## MeetingFragment:onLoadFinished");
            if (mPosition != ListView.INVALID_POSITION) {
                // If we don't need to restart the loader, and there's a desired position to restore
                // to, do so now.
                mListView.smoothScrollToPosition(mPosition);
            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> cursorLoader) {
            mMeetingAdapter.swapCursor(null);
        }


}
