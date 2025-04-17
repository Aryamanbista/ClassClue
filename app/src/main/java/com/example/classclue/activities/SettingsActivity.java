package com.example.classclue.activities;

import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.classclue.R;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Display the fragment as the main content
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }

    public static class SettingsFragment extends android.preference.PreferenceFragment
            implements SharedPreferences.OnSharedPreferenceChangeListener {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // Load the preferences from an XML resource
            addPreferencesFromResource(R.xml.preferences);

            // Set summary for current alarm tone preference
            updateAlarmToneSummary();
        }

        @Override
        public void onResume() {
            super.onResume();
            getPreferenceManager().getSharedPreferences()
                    .registerOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onPause() {
            super.onPause();
            getPreferenceManager().getSharedPreferences()
                    .unregisterOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            if (key.equals(getString(R.string.pref_alarm_tone_key))) {
                updateAlarmToneSummary();
                Toast.makeText(getActivity(), "Alarm tone updated", Toast.LENGTH_SHORT).show();
            } else if (key.equals(getString(R.string.pref_24h_reminder_key))) {
                boolean enabled = sharedPreferences.getBoolean(key, true);
                String message = "24-hour reminders " + (enabled ? "enabled" : "disabled");
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
            } else if (key.equals(getString(R.string.pref_2h_reminder_key))) {
                boolean enabled = sharedPreferences.getBoolean(key, true);
                String message = "2-hour reminders " + (enabled ? "enabled" : "disabled");
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
            }
        }

        private void updateAlarmToneSummary() {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
            String alarmToneUri = sharedPreferences.getString(
                    getString(R.string.pref_alarm_tone_key),
                    RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM).toString());

            Ringtone ringtone = RingtoneManager.getRingtone(
                    getActivity(), Uri.parse(alarmToneUri));

            String name = ringtone.getTitle(getActivity());
            findPreference(getString(R.string.pref_alarm_tone_key))
                    .setSummary(name);
        }
    }
}