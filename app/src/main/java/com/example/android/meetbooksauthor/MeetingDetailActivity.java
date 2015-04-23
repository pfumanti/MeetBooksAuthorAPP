package com.example.android.meetbooksauthor;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

//import com.example.android.meetbooksauthor..data.MeetingContract.MeetingEntry;


public class MeetingDetailActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_detail);


        if (savedInstanceState == null) {

            Bundle arguments = new Bundle();
            //Read the data from yhe incomin intent
            arguments.putParcelable(MeetingDetailFragment.DETAIL_URI, getIntent().getData());

            //We take the URi and set as argument in the new MeetingDetailFragment
            MeetingDetailFragment fragment = new MeetingDetailFragment();
            fragment.setArguments(arguments);

            //Add dynamically the above fragnment to this container
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.meeting_detail_container, fragment)
                    .commit();

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_meeting_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            startActivity(new Intent(this, MeetingSettingActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
