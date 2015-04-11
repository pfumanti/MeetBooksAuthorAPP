package com.example.android.meetbooksauthor;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.meetbooksauthor.data.MeetingContract;

//import android.widget.CursorAdapter;

/**
 * Created by Administrator on 09/04/2015.
 * ADD a CursorAdapter to app
 * So loader will be making the calls
 * on our content provider to get a Cursor and we’ll want to take the data from that cursor
 * and put it into our UI. This is what adapters are meant for, associating data with UI components.
 *
 * /**
 + * {@link MeetingAdapter} exposes a list of meeting forecasts
 + * from a {@link android.database.Cursor} to a {@link android.widget.ListView}.
 + */

public class MeetingAdapter extends CursorAdapter {

    public MeetingAdapter(Context context, Cursor c, int flags) {
            super(context, c, flags);
    }

    /*
    private String convertCursorRowToUXFormat(Cursor cursor) {
        //TMP per ora ferma 30
        // return strings to keep UI functional for now

        int idx_book_title =   cursor.getColumnIndex(MeetingContract.MeetingEntry.COLUMN_BOOK_TITLE);
        int idx_author_name =  cursor.getColumnIndex(MeetingContract.MeetingEntry.COLUMN_AUTHOR_NAME);
        //int idx_tour_start_time =  cursor.getColumnIndex(MeetingContract.MeetingEntry.COLUMN_TOUR_START_TIME);

        return cursor.getString(idx_book_title)+ " - " +
               cursor.getString(idx_author_name) + " - " +
               cursor.getString(MeetingFragment.COL_TOUR_START_TIME);

    }
    */

    //Must implement method bindView(View, Context,Cursor)
    /*
+ Remember that these views are reused as needed.
+ */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_meeting, parent, false);
            return view;
    }

    /*
        + This is where we fill-in the views with the contents of the cursor.
        + */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // our view is pretty simple here --- just a text view
        // we'll keep the UI functional with a simple (and slow!) binding.
        //TMP
        //TextView tv = (TextView)view;
        //tv.setText(convertCursorRowToUXFormat(cursor));
        int idx_book_title =  cursor.getColumnIndex(MeetingContract.MeetingEntry.COLUMN_BOOK_TITLE);
        String bookTitle = cursor.getString(idx_book_title);
        TextView bookTitleView = (TextView) view.findViewById(R.id.list_item_book_textview);
        bookTitleView.setText(bookTitle);

        int idx_author_name =  cursor.getColumnIndex(MeetingContract.MeetingEntry.COLUMN_AUTHOR_NAME);
        String authorName = cursor.getString(idx_author_name);
        TextView authorNameView = (TextView) view.findViewById(R.id.list_item_author_textview);
        authorNameView.setText(authorName);

        int idx_start_day  = cursor.getColumnIndex(MeetingContract.MeetingEntry.COLUMN_TOUR_START_TIME);
        String startDay = cursor.getString(idx_start_day);
        TextView startDayView = (TextView) view.findViewById(R.id.list_item_start_date_textview);
        startDayView.setText(startDay);


        int idx_city = cursor.getColumnIndex(MeetingContract.MeetingEntry.COLUMN_CITY);
        String city = cursor.getString(idx_city);
        TextView cityView = (TextView) view.findViewById(R.id.list_item_city_textview);
        cityView.setText(city);


        int idx_state = cursor.getColumnIndex(MeetingContract.MeetingEntry.COLUMN_STATE_ID);
        String stateId = cursor.getString(idx_state);
        ImageView iconView = (ImageView) view.findViewById(R.id.list_item_state_icon);
        iconView.setImageResource(R.drawable.ma);
        TextView stateIdView = (TextView) view.findViewById(R.id.list_item_state_textview);
        stateIdView.setText(stateId);


    }

}
