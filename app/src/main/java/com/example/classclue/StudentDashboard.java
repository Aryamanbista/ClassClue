package com.example.classclue;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import com.example.classclue.Event; // Ensure this is the only Event import

public class StudentDashboard extends AppCompatActivity {
    private AppDatabase appDb;
    private TextView greetingTextView, nextDeadlineTextView;
    private RecyclerView subjectsRecyclerView, eventsRecyclerView;
    private SubjectSummaryAdapter subjectAdapter;
    private EventAdapter eventAdapter;
    private List<SubjectSummary> subjectSummaries = new ArrayList<>();
    private List<Event> events = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_dashboard);

        // Initialize Room database
        appDb = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "events-db")
                .allowMainThreadQueries().build();

        // Bind UI elements
        greetingTextView = findViewById(R.id.greetingTextView);
        nextDeadlineTextView = findViewById(R.id.nextDeadlineTextView);
        subjectsRecyclerView = findViewById(R.id.subjectsRecyclerView);
        eventsRecyclerView = findViewById(R.id.eventsRecyclerView);
        ImageView settingsIcon = findViewById(R.id.settingsIcon);

        // Set up greeting (hardcoded for now, can be fetched from user profile)
        greetingTextView.setText("Hello, Aryaman\nTrack your academic progress");

        // Set up settings icon click listener
        settingsIcon.setOnClickListener(v -> {
            startActivity(new Intent(this, SettingsActivity.class));
        });

        // Set up RecyclerViews
        subjectsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        subjectAdapter = new SubjectSummaryAdapter(subjectSummaries);
        subjectsRecyclerView.setAdapter(subjectAdapter);

        eventsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        eventAdapter = new EventAdapter(events);
        eventsRecyclerView.setAdapter(eventAdapter);

        // Load data and schedule alarms
        loadDashboardData();
    }

    private void loadDashboardData() {
        // Fetch all events from Room database
        events.clear();
        events.addAll(appDb.eventDao().getAll());
        eventAdapter.notifyDataSetChanged();

        // Find next deadline
        if (!events.isEmpty()) {
            Event nextEvent = findNextEvent(events);
            nextDeadlineTextView.setText(String.format("%s\n%s, %s",
                    nextEvent.getTitle(), nextEvent.getSubject(), formatEventDate(nextEvent.getDate())));
            scheduleAlarms(nextEvent);
        } else {
            nextDeadlineTextView.setText("No upcoming deadlines");
        }

        // Summarize subjects and task counts
        subjectSummaries.clear();
        Map<String, Long> subjectTaskCount = events.stream()
                .collect(Collectors.groupingBy(Event::getSubject, Collectors.counting()));
        subjectTaskCount.forEach((subject, count) ->
                subjectSummaries.add(new SubjectSummary(subject, count.intValue())));
        subjectAdapter.notifyDataSetChanged();
    }

    private Event findNextEvent(List<Event> events) {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy - hh:mm a", Locale.US);
        Event nextEvent = events.get(0);
        try {
            Date nextDate = sdf.parse(nextEvent.getDate());
            for (Event event : events) {
                Date eventDate =sdf.parse(event.getDate());
                if (eventDate.before(nextDate)) {
                    nextEvent = event;
                    nextDate = eventDate;
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return nextEvent;
    }

    private String formatEventDate(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy - hh:mm a", Locale.US);
        try {
            Date eventDate = sdf.parse(date);
            long diff = eventDate.getTime() - System.currentTimeMillis();
            if (diff <= 24 * 60 * 60 * 1000) {
                return "Tomorrow at " + new SimpleDateFormat("hh:mm a", Locale.US).format(eventDate);
            }
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
            return date;
        }
    }

    private void scheduleAlarms(Event event) {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy - hh:mm a", Locale.US);
        try {
            Date eventDate = sdf.parse(event.getDate());
            long currentTime = System.currentTimeMillis();
            long eventTime = eventDate.getTime();

            // Schedule alarm 24 hours before
            long delay24Hours = eventTime - currentTime - 24 * 60 * 60 * 1000;
            if (delay24Hours > 0) {
                Data data = new Data.Builder()
                        .putString("eventTitle", event.getTitle())
                        .build();
                OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(AlarmWorker.class)
                        .setInitialDelay(delay24Hours, TimeUnit.MILLISECONDS)
                        .setInputData(data)
                        .build();
                WorkManager.getInstance(this).enqueue(workRequest);
            }

            // Schedule alarm 2 hours before
            long delay2Hours = eventTime - currentTime - 2 * 60 * 60 * 1000;
            if (delay2Hours > 0) {
                Data data = new Data.Builder()
                        .putString("eventTitle", event.getTitle())
                        .build();
                OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(AlarmWorker.class)
                        .setInitialDelay(delay2Hours, TimeUnit.MILLISECONDS)
                        .setInputData(data)
                        .build();
                WorkManager.getInstance(this).enqueue(workRequest);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}