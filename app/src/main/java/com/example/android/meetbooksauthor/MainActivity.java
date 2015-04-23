package com.example.android.meetbooksauthor;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends ActionBarActivity implements MeetingFragment.Callback {

    private final String LOG_TAG = MainActivity.class.getSimpleName();

    private static final String DETAILFRAGMENT_TAG = "DFTAG";

    private boolean mTwoPane;

    private final String MEETING_STATE_FRAGMENT_TAG = "XX";
    private final String MEETING_NEXT_DAY_FRAGMENT_TAG = "XX";

    private String stateOfMeeting;
    private String nextDaysMeeting;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v(LOG_TAG,"in onCreate");

        stateOfMeeting = Uty.getStateLocation(this);


        Log.v(LOG_TAG,"in onCreate stateOfMeeting = "+ stateOfMeeting);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (findViewById(R.id.meeting_detail_container) != null) {//I have a large screen !!!
            mTwoPane = true;
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.meeting_detail_container, new MeetingDetailFragment(), DETAILFRAGMENT_TAG)
                        .commit();
            }
        } else {
            mTwoPane = false;
            getSupportActionBar().setElevation(0f);
        }
        //Ci vuole ??
    //   MeetingFragment meetingFragment = ((MeetingFragment)getSupportFragmentManager()
    //            .findFragmentById(R.id.fragment_meeting));

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //If "Setting" item is selected ....
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, MeetingSettingActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    @Override
    protected void onStart() {
        Log.v(LOG_TAG, "in onStart");
        super.onStart();
// The activity is about to become visible.
    }



    protected void onResume() {
        Log.v(LOG_TAG, "####in onResume");
        super.onResume();
        String location = Uty.getStateLocation(this);
        // update the location in our second pane using the fragment manager
        if (location != null && !location.equals(stateOfMeeting)) {
            MeetingFragment mf = (MeetingFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_meeting);
            if (null != mf) {
                mf.onLocationChanged();
            }
            MeetingDetailFragment df = (MeetingDetailFragment)getSupportFragmentManager().findFragmentByTag(DETAILFRAGMENT_TAG);
            if ( null != df ) {
                df.onLocationChanged(location);
            }

            stateOfMeeting = location;
        }
    }

    @Override
    public void onItemSelected(Uri contentUri) {
        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
              // adding or replacing the detail fragment using a
                            // fragment transaction.
            Bundle args = new Bundle();
            args.putParcelable(MeetingDetailFragment.DETAIL_URI, contentUri);

            MeetingDetailFragment fragment = new MeetingDetailFragment();
            fragment.setArguments(args);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.meeting_detail_container, fragment, DETAILFRAGMENT_TAG)
                    .commit();
        } else {
            Log.d(LOG_TAG, "############# " + "Main onItemSelected onePanle" );
            Intent intent = new Intent(this, MeetingDetailActivity.class)
                    .setData(contentUri);
            startActivity(intent);
            }
        }



    @Override
    protected void onPause() {
        Log.v(LOG_TAG, "#####in onPause");
        super.onPause();
// Another activity is taking focus (this activity is about to be "paused").
    }
    @Override
    protected void onStop() {
        Log.v(LOG_TAG, "####in onStop");
        super.onStop();
// The activity is no longer visible (it is now "stopped")
    }
    @Override
    protected void onDestroy() {
        Log.v(LOG_TAG, "####in onDestroy");
        super.onDestroy();
// The activity is about to be destroyed.
    }

}
