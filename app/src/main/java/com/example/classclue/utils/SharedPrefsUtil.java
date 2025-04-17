package com.example.classclue.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.preference.PreferenceManager;

import com.example.classclue.R;

public class SharedPrefsUtil {

    public static boolean is24hReminderEnabled(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getBoolean(context.getString(R.string.pref_24h_reminder_key), true);
    }

    public static boolean is2hReminderEnabled(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getBoolean(context.getString(R.string.pref_2h_reminder_key), true);
    }

    public static Uri getAlarmToneUri(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String uriString = prefs.getString(context.getString(R.string.pref_alarm_tone_key),
                RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM).toString());
        return Uri.parse(uriString);
    }

    public static int getAlarmVolumeLevel(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return Integer.parseInt(prefs.getString(context.getString(R.string.pref_alarm_volume_key), "3"));
    }
}