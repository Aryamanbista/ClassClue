package com.example.classclue.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.example.classclue.models.Event;
import com.example.classclue.services.AlarmReceiver;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AlarmUtils {
    private static final SimpleDateFormat dateTimeFormat =
            new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());

    public static void scheduleEventAlarms(Context context, Event event) {
        try {
            // Parse event date and time
            Date eventDateTime = dateTimeFormat.parse(event.getDate() + " " + event.getTime());
            if (eventDateTime == null) return;

            Calendar eventCal = Calendar.getInstance();
            eventCal.setTime(eventDateTime);

            // Schedule 24-hour reminder if enabled
            if (SharedPrefsUtil.is24hReminderEnabled(context)) {
                scheduleAlarm(context, event, eventCal, -24 * 60, "24-hour");
            }

            // Schedule 2-hour reminder if enabled
            if (SharedPrefsUtil.is2hReminderEnabled(context)) {
                scheduleAlarm(context, event, eventCal, -2 * 60, "2-hour");
            }

        } catch (ParseException e) {
            Log.e("AlarmUtils", "Error parsing event date/time", e);
        }
    }

    private static void scheduleAlarm(Context context, Event event, Calendar eventCal,
                                      int minutesBefore, String reminderType) {
        Calendar alarmCal = (Calendar) eventCal.clone();
        alarmCal.add(Calendar.MINUTE, minutesBefore);

        // Don't schedule if the reminder time has already passed
        if (alarmCal.before(Calendar.getInstance())) {
            return;
        }

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra("event", event);
        intent.putExtra("reminderType", reminderType);

        int requestCode = (event.getId() + reminderType).hashCode();
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                requestCode,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        // Use setExactAndAllowWhileIdle for precise timing (Android 6.0+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    alarmCal.getTimeInMillis(),
                    pendingIntent);
        } else {
            alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    alarmCal.getTimeInMillis(),
                    pendingIntent);
        }
    }

    public static void cancelEventAlarms(Context context, Event event) {
        cancelAlarm(context, event, "24-hour");
        cancelAlarm(context, event, "2-hour");
    }

    private static void cancelAlarm(Context context, Event event, String reminderType) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);

        int requestCode = (event.getId() + reminderType).hashCode();
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                requestCode,
                intent,
                PendingIntent.FLAG_NO_CREATE | PendingIntent.FLAG_IMMUTABLE);

        if (pendingIntent != null) {
            alarmManager.cancel(pendingIntent);
            pendingIntent.cancel();
        }
    }
}