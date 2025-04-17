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
import com.example.classclue.adapters.SubjectAdapter;
import com.example.classclue.models.Subject;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class SubjectListActivity extends AppCompatActivity {

    private static final int ADD_SUBJECT_REQUEST = 1;
    private static final int EDIT_SUBJECT_REQUEST = 2;

    private RecyclerView recyclerView;
    private SubjectAdapter adapter;
    private List<Subject> subjectList;
    private boolean isAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_list);

        // Check if user is admin (from intent extras)
        isAdmin = getIntent().getBooleanExtra("isAdmin", false);

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recyclerViewSubjects);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Sample data - replace with actual data from Firebase later
        subjectList = new ArrayList<>();
        subjectList.add(new Subject("S1", "Mobile Application Development", "BIT219"));
        subjectList.add(new Subject("S2", "Data Structures", "BIT221"));

        adapter = new SubjectAdapter(subjectList, isAdmin, new SubjectAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Subject subject) {
                if (isAdmin) {
                    // Open subject for editing
                    Intent intent = new Intent(SubjectListActivity.this, AddEditSubjectActivity.class);
                    intent.putExtra("subject", subject);
                    startActivityForResult(intent, EDIT_SUBJECT_REQUEST);
                } else {
                    // Open events for this subject
                    Intent intent = new Intent(SubjectListActivity.this, EventListActivity.class);
                    intent.putExtra("subjectId", subject.getId());
                    startActivity(intent);
                }
            }
        });
        recyclerView.setAdapter(adapter);

        // FAB for adding new subject (only visible to admin)
        FloatingActionButton fabAdd = findViewById(R.id.fabAddSubject);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAdmin) {
                    startActivityForResult(new Intent(SubjectListActivity.this,
                            AddEditSubjectActivity.class), ADD_SUBJECT_REQUEST);
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
            Subject subject = (Subject) data.getSerializableExtra("subject");

            if (subject != null) {
                if (requestCode == ADD_SUBJECT_REQUEST) {
                    // Generate a unique ID for new subjects (replace with Firebase ID later)
                    subject.setId("SUB" + System.currentTimeMillis());
                    subjectList.add(subject);
                    Toast.makeText(this, "Subject added successfully", Toast.LENGTH_SHORT).show();
                } else if (requestCode == EDIT_SUBJECT_REQUEST) {
                    int position = data.getIntExtra("position", -1);
                    if (position != -1) {
                        subjectList.set(position, subject);
                        Toast.makeText(this, "Subject updated successfully", Toast.LENGTH_SHORT).show();
                    }
                }
                adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_subject_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_refresh) {
            // Refresh subject list
            Toast.makeText(this, "Refreshing subjects...", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}