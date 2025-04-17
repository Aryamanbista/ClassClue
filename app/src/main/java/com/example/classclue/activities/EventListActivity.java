package com.example.classclue.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.classclue.R;
import com.example.classclue.adapters.EventAdapter;
import com.example.classclue.models.Event;
import com.example.classclue.models.Subject;
import com.example.classclue.utils.AlarmUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class EventListActivity extends AppCompatActivity {

    private static final int ADD_EVENT_REQUEST = 1;
    private static final int EDIT_EVENT_REQUEST = 2;

    private RecyclerView recyclerView;
    private EventAdapter adapter;
    private List<Event> eventList;
    private Subject subject;
    private boolean isAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);

        // Get subject from intent
        subject = (Subject) getIntent().getSerializableExtra("subject");
        isAdmin = getIntent().getBooleanExtra("isAdmin", false);

        if (subject == null) {
            Toast.makeText(this, "Please select a subject first", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }


        setTitle(subject.getName() + " Events");

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recyclerViewEvents);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Sample data - replace with actual data from Firebase later
        eventList = new ArrayList<>();
        eventList.add(new Event("E1", "Midterm Exam", "Exam", "2025-05-15", "09:00", "LT1"));
        eventList.add(new Event("E2", "Assignment 1 Due", "Assignment", "2025-04-20", "23:59", "Online"));

        adapter = new EventAdapter(eventList, isAdmin, new EventAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Event event) {
                if (isAdmin) {
                    // Open event for editing
                    Intent intent = new Intent(EventListActivity.this, AddEditEventActivity.class);
                    intent.putExtra("event", event);
                    intent.putExtra("subjectId", subject.getId());
                    startActivityForResult(intent, EDIT_EVENT_REQUEST);
                }
            }
        });
        recyclerView.setAdapter(adapter);

        // FAB for adding new event (only visible to admin)
        FloatingActionButton fabAdd = findViewById(R.id.fabAddEvent);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAdmin) {
                    Intent intent = new Intent(EventListActivity.this, AddEditEventActivity.class);
                    intent.putExtra("subjectId", subject.getId());
                    startActivityForResult(intent, ADD_EVENT_REQUEST);
                }
            }
        });

        // Hide FAB if not admin
        fabAdd.setVisibility(isAdmin ? View.VISIBLE : View.GONE);
    }

    // In onActivityResult:
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            Event event = (Event) data.getSerializableExtra("event");

            if (event != null) {
                if (requestCode == ADD_EVENT_REQUEST) {
                    // Generate a unique ID for new events (replace with Firebase ID later)
                    event.setId("EVT" + System.currentTimeMillis());
                    event.setSubjectId(subject.getId());
                    eventList.add(event);
                    Toast.makeText(this, "Event added successfully", Toast.LENGTH_SHORT).show();

                    // Schedule alarms for the new event
                    AlarmUtils.scheduleEventAlarms(this, event);
                } else if (requestCode == EDIT_EVENT_REQUEST) {
                    int position = data.getIntExtra("position", -1);
                    if (position != -1) {
                        // Cancel existing alarms before updating
                        AlarmUtils.cancelEventAlarms(this, eventList.get(position));

                        eventList.set(position, event);
                        Toast.makeText(this, "Event updated successfully", Toast.LENGTH_SHORT).show();

                        // Schedule new alarms with updated event data
                        AlarmUtils.scheduleEventAlarms(this, event);
                    }
                }
                adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_event_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_refresh) {
            // Refresh event list
            Toast.makeText(this, "Refreshing events...", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Add this method to handle event deletion
    private void deleteEvent(int position) {
        Event event = eventList.get(position);
        // Cancel alarms for this event
        AlarmUtils.cancelEventAlarms(this, event);
        // Remove from list and update adapter
        eventList.remove(position);
        adapter.notifyItemRemoved(position);
    }
}