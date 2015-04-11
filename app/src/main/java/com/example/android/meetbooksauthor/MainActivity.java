package com.example.android.meetbooksauthor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends ActionBarActivity {

    private final String LOG_TAG = MainActivity.class.getSimpleName();
    private final String MEETING_STATE_FRAGMENT_TAG = "XX";
    private final String MEETING_NEXT_DAY_FRAGMENT_TAG = "XX";

    private String stateOfMeeting;
    private String nextDaysMeeting;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v(LOG_TAG,"in onCreate");
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        stateOfMeeting = prefs.getString(this.getString(R.string.pref_states_key),
                        this.getString(R.string.pref_states_any));
        Log.v(LOG_TAG,"in onCreate stateOfMeeting = "+ stateOfMeeting);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                   // .add(R.id.container, new MeetingFragment())
                    .add(R.id.container, new MeetingFragment(), MEETING_STATE_FRAGMENT_TAG)
                    .commit();
            /*
            getSupportFragmentManager().beginTransaction()
                    //.add(R.id.container, new MeetingFragment())
                    .add(R.id.container, new MeetingFragment(), MEETING_NEXT_DAY_FRAGMENT_TAG)
                    .commit();*/
        }
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

    /**
     * A placeholder fragment containing a simple view.
     */
    /*
    public static class PlaceholderFragment extends Fragment {

        ArrayAdapter<String> mMeetingAdapter;

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            // Create some dummy data for the ListView. Here's a sample weekly forecast
            String[] data = {
                    "Mon 6/23 - Charles Simic - Washington",
                    "Tue 6/24 - Susan Sarandon - New York",
                    "Wed 6/25 - Bill crystal - New York",
                    "Thurs 6/26 - Rainy - Boston"
            };
            List <String> meetingList = new ArrayList<String>(Arrays.asList(data));

            // Now that we have some dummy data, create an ArrayAdapter.
            // The ArrayAdapter will take data from a source (like our dummy meetingList) and
            // use it to populate the ListView it's attached to.
            mMeetingAdapter =
                    new ArrayAdapter<String>(
                            getActivity(), // The current context (this activity)
                            R.layout.list_item_meeting, // The name of the layout ID.
                            R.id.list_item_meeting_textview, // The ID of the textview to populate.
                            meetingList);

            //Here we reference our UI layout resource ("fragment_main.xml")
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);

            //Bind tra ListView e Adapter
            //First find List view (in fragment)
            ListView listView = (ListView) rootView.findViewById(R.id.listview_meeting);
            //Attach Adapter to List View
            listView.setAdapter(mMeetingAdapter);

            //Il rootView si riferisce a quello di cui abbiiamo fatto l'inflate
            return rootView;
        }
    }
    */

    @Override
    protected void onStart() {
        Log.v(LOG_TAG, "in onStart");
        super.onStart();
// The activity is about to become visible.
    }
    @Override
    protected void onResume() {
        Log.v(LOG_TAG, "in onResume");
        super.onResume();
        String stateId = "oo";
        //String stateId = Utility.getPreferredLocation( this );
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        stateId = prefs.getString(this.getString(R.string.pref_states_key), this.getString(R.string.pref_states_any));

        // update the location in our second pane using the fragment manager
        if (stateId != null && !stateId.equals(stateOfMeeting)) {
            MeetingFragment ff = (MeetingFragment)getSupportFragmentManager().findFragmentByTag(MEETING_STATE_FRAGMENT_TAG);
            if ( null != ff ) {
                ff.onLocationChanged();
            }
            stateOfMeeting = stateId;
        }
// The activity has become visible (it is now "resumed").
    }
    @Override
    protected void onPause() {
        Log.v(LOG_TAG, "in onPause");
        super.onPause();
// Another activity is taking focus (this activity is about to be "paused").
    }
    @Override
    protected void onStop() {
        Log.v(LOG_TAG, "in onStop");
        super.onStop();
// The activity is no longer visible (it is now "stopped")
    }
    @Override
    protected void onDestroy() {
        Log.v(LOG_TAG, "in onDestroy");
        super.onDestroy();
// The activity is about to be destroyed.
    }

}
