package com.example.android.meetbooksauthor;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Administrator on 15/04/2015.
 */
public class Uty {

    public static String getStateLocation(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(context.getString(R.string.pref_states_key),context.getString(R.string.pref_states_default));
    }

    public static String getRemainingDays(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(context.getString(R.string.pref_remaining_days_key),
                context.getString(R.string.pref_remaining_days_default));
    }

    public static int getIconResourceForStateLocation(String state) {
// Based on weather code data found at:
// http://bugs.openweathermap.org/projects/api/wiki/Weather_Condition_Codes
        if (state.equalsIgnoreCase("MA")) {
            return R.drawable.ma;
        } else if (state.equalsIgnoreCase("DC")) {
            return R.drawable.ma;

        }
        return -1;
    }


}
