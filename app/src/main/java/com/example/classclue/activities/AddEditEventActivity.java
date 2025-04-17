package com.example.classclue.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.classclue.R;
import com.example.classclue.models.Event;
import com.example.classclue.utils.AlarmUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class AddEditEventActivity extends AppCompatActivity {

    private EditText etEventName, etEventDate, etEventTime, etEventLocation;
    private Spinner spinnerEventType;
    private Event event;
    private boolean isEditMode = false;
    private int position;
    private String subjectId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_event);

        etEventName = findViewById(R.id.etEventName);
        etEventDate = findViewById(R.id.etEventDate);
        etEventTime = findViewById(R.id.etEventTime);
        etEventLocation = findViewById(R.id.etEventLocation);
        spinnerEventType = findViewById(R.id.spinnerEventType);

        // Setup event type spinner
        List<String> eventTypes = Arrays.asList("Exam", "Test", "Assignment", "Lab", "Other");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, eventTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEventType.setAdapter(adapter);

        // Get subject ID
        subjectId = getIntent().getStringExtra("subjectId");
        if (subjectId == null) {
            Toast.makeText(this, "Subject not specified", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Check if we're in edit mode
        if (getIntent().hasExtra("event")) {
            event = (Event) getIntent().getSerializableExtra("event");
            position = getIntent().getIntExtra("position", -1);
            if (event != null) {
                isEditMode = true;
                etEventName.setText(event.getName());
                etEventDate.setText(event.getDate());
                etEventTime.setText(event.getTime());
                etEventLocation.setText(event.getLocation());

                int spinnerPosition = adapter.getPosition(event.getType());
                spinnerEventType.setSelection(spinnerPosition);

                setTitle("Edit Event");
            }
        } else {
            setTitle("Add Event");
            event = new Event();
            event.setSubjectId(subjectId);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_save, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_save) {
            saveEvent();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveEvent() {
        String name = etEventName.getText().toString().trim();
        String date = etEventDate.getText().toString().trim();
        String time = etEventTime.getText().toString().trim();
        String location = etEventLocation.getText().toString().trim();
        String type = spinnerEventType.getSelectedItem().toString();

        if (name.isEmpty() || date.isEmpty() || time.isEmpty() || location.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isValidDate(date)) {
            etEventDate.setError("Please use YYYY-MM-DD format");
            return;
        }

        if (!isValidTime(time)) {
            etEventTime.setError("Please use HH:MM format (24-hour)");
            return;
        }

        event.setName(name);
        event.setType(type);
        event.setDate(date);
        event.setTime(time);
        event.setLocation(location);

        Intent resultIntent = new Intent();
        resultIntent.putExtra("event", event);
        if (isEditMode) {
            resultIntent.putExtra("position", getIntent().getIntExtra("position", -1));
        }
        setResult(RESULT_OK, resultIntent);
        Toast.makeText(this, "Event saved", Toast.LENGTH_SHORT).show();
        finish();
    }

    private boolean isValidDate(String date) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            sdf.setLenient(false);
            sdf.parse(date);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    private boolean isValidTime(String time) {
        return time.matches("^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$");
    }
}