package com.example.classclue.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.classclue.R;
import com.example.classclue.activities.EventListActivity;
import com.example.classclue.models.Event;
import com.example.classclue.utils.SharedPrefsUtil;

public class AlarmReceiver extends BroadcastReceiver {
    private static final String CHANNEL_ID = "event_reminder_channel";
    private static final int NOTIFICATION_ID = 100;

    @Override
    public void onReceive(Context context, Intent intent) {
        Event event = (Event) intent.getSerializableExtra("event");
        String reminderType = intent.getStringExtra("reminderType");

        if (event == null) return;

        // Create notification channel (required for Android 8.0+)
        createNotificationChannel(context);

        // Prepare intent for notification click action
        Intent notificationIntent = new Intent(context, EventListActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                0,
                notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        // Build notification
        String notificationTitle = "Upcoming " + event.getType() + ": " + event.getName();
        String notificationText = String.format("%s at %s (%s)",
                event.getDate(), event.getTime(), event.getLocation());

        if (reminderType != null) {
            notificationText = reminderType + " reminder - " + notificationText;
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(notificationTitle)
                .setContentText(notificationText)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        // Set alarm sound from preferences
        Uri alarmSound = SharedPrefsUtil.getAlarmToneUri(context);
        builder.setSound(alarmSound);

        // Show notification
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, builder.build());

        // Also play the alarm sound immediately
        playAlarmSound(context, alarmSound);
    }

    private void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Event Reminders";
            String description = "Channel for event reminder notifications";
            int importance = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager =
                    context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void playAlarmSound(Context context, Uri alarmSound) {
        try {
            Ringtone ringtone = RingtoneManager.getRingtone(context, alarmSound);
            if (ringtone != null) {
                ringtone.play();
            }
        } catch (Exception e) {
            Log.e("AlarmReceiver", "Error playing alarm sound", e);
        }
    }
}