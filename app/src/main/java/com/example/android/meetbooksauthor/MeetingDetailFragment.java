package com.example.android.meetbooksauthor;

/**
 * Created by Administrator on 15/04/2015.
 */

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.meetbooksauthor.data.MeetingContract;

/**
 * A placeholder fragment containing a simple view.
 */

public  class MeetingDetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {


    //private String mMeetingStr;
    private static final String MEETING_SHARE_HASHTAG = " #MeetBookAuthorsApp";

    static final String DETAIL_URI = "URI";

    private static final String LOG_TAG = MeetingDetailFragment.class.getSimpleName();

    private ShareActionProvider mShareActionProvider;
    private String mMeeting;
    private Uri myUri;

    //id of Cursor lOADER
    //use a different id for each cursor in our activity
    private static final int MEETING_DETAIL_CURSOR_LOADER = 0;

    private static final String[] MEETING_DETAILS_COLUMNS = {
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

    // these constants correspond to the projection defined above, and must change if the
    // projection changes
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


    private ImageView mIconView;
    private TextView authorDView;
    private TextView bookDView;
    private TextView cityDView;
    private TextView stateDView;
    private TextView venueDView;
    private TextView addressDView;
    private TextView timeDView;
    private TextView bookingDateDView;




    public MeetingDetailFragment() {
        //Add menu
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_meeting_detail, container, false);

        Bundle arguments = getArguments();
        if (arguments != null) {
             myUri = arguments.getParcelable(MeetingDetailFragment.DETAIL_URI);
        }

        mIconView = (ImageView) rootView.findViewById(R.id.detail_state_icon);
        authorDView = (TextView) rootView.findViewById(R.id.detail_author_textview);
        bookDView = (TextView) rootView.findViewById(R.id.detail_book_textview);
        cityDView = (TextView) rootView.findViewById(R.id.detail_city_textview);
        stateDView = (TextView) rootView.findViewById(R.id.detail_state_textview);
        venueDView = (TextView) rootView.findViewById(R.id.detail_venue_textview);
        addressDView = (TextView) rootView.findViewById(R.id.detail_address_textview);
        timeDView = (TextView) rootView.findViewById(R.id.detail_time_textview);
        bookingDateDView = (TextView) rootView.findViewById(R.id.detail_booking_date_textview);

        return rootView;

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
// Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.meeting_detail_fragment, menu);
// Retrieve the share menu item
        MenuItem menuItem = menu.findItem(R.id.action_share);

        // Get the provider and hold onto it to set/change the share intent.
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);
// If onLoadFinished happens before this, we can go ahead and set the share intent now.
        if (mMeeting != null) {
            mShareActionProvider.setShareIntent(createShareMeetingIntent());
        }

    }


    private Intent createShareMeetingIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);

        //FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET prevents from putting our intent on top
        // of activities stack. If you dont'use this flag, you could comes back in another
        //  application instead of this one
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT,
                mMeeting + MEETING_SHARE_HASHTAG);
        return shareIntent;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(MEETING_DETAIL_CURSOR_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    void onLocationChanged( String newLocation ) {
         // replace the uri, since the location has changed
                Uri uri = myUri;
        if (null != uri) {

            Uri updatedUri = MeetingContract.MeetingEntry.buildMeetingLocation(newLocation);
            myUri = updatedUri;
            getLoaderManager().restartLoader(MEETING_DETAIL_CURSOR_LOADER, null, this);
            }
        }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.v(LOG_TAG, "####In MeetingDetailFragment onCreateLoader");
        if (null != myUri) {
            return new CursorLoader(
                    getActivity(),
                    myUri,
                    MEETING_DETAILS_COLUMNS,
                    null,
                    null,
                    null
            );

        }
        return null;
       // Intent intent = getActivity().getIntent();
       // //if (intent == null) {
       // if (intent == null || intent.getData() == null) {
       //     return null;
       // }
        /*
// Now create and return a CursorLoader that will take care of
// creating a Cursor for the data being displayed.
        return new CursorLoader(
                getActivity(),
                intent.getData(),
                MEETING_DETAILS_COLUMNS,
                null,
                null,
                null
        );
        */
    }
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.v(LOG_TAG, "DETAIL In onLoadFinished");
        if (!data.moveToFirst()) { return; }

        mMeeting = String.format("%s - %s - %s", data.getString(COL_AUTHOR_NAME),data.getString(COL_BOOK_TITLE),
                data.getString(COL_TOUR_START_TIME));

        String authorName = data.getString(COL_AUTHOR_NAME);
        authorDView.setText(authorName);

        String bookTitle = data.getString(COL_BOOK_TITLE);
        bookDView.setText(bookTitle);

        String city = data.getString(COL_CITY);
        cityDView.setText(city);

        String state = data.getString(COL_STATE_ID);
        stateDView.setText(state);

        //mIconView.setImageResource(R.drawable.ma);

        mIconView.setImageResource(Uty.getIconResourceForStateLocation(state));

        String venue = data.getString(COL_VENUE);
        venueDView.setText(venue);
        String address = data.getString(COL_ADDRESS);
        addressDView.setText(address);
        String time = data.getString(COL_TIMEOFDAY);
        stateDView.setText(time);
        String bookingDate = data.getString(COL_BOOKING_DATE);
        bookingDateDView.setText(bookingDate);


        // If onCreateOptionsMenu has already happened, we need to update the share intent now.
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(createShareMeetingIntent());
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) { }
}
