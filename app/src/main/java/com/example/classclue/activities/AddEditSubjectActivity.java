package com.example.classclue.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.classclue.R;
import com.example.classclue.models.Subject;

public class AddEditSubjectActivity extends AppCompatActivity {

    private EditText etSubjectName, etSubjectCode;
    private Subject subject;
    private boolean isEditMode = false;
    private int position;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_subject);

        etSubjectName = findViewById(R.id.etSubjectName);
        etSubjectCode = findViewById(R.id.etSubjectCode);

        // Check if we're in edit mode
        if (getIntent().hasExtra("subject")) {
            subject = (Subject) getIntent().getSerializableExtra("subject");
            position = getIntent().getIntExtra("position", -1);
            if (subject != null) {
                isEditMode = true;
                etSubjectName.setText(subject.getName());
                etSubjectCode.setText(subject.getCode());
                setTitle("Edit Subject");
            }
        } else {
            setTitle("Add Subject");
            subject = new Subject();
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
            saveSubject();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveSubject() {
        String name = etSubjectName.getText().toString().trim();
        String code = etSubjectCode.getText().toString().trim();

        if (name.isEmpty() || code.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        subject.setName(name);
        subject.setCode(code);

        Intent resultIntent = new Intent();
        resultIntent.putExtra("subject", subject);
        if (isEditMode) {
            resultIntent.putExtra("position", getIntent().getIntExtra("position", -1));
        }
        setResult(RESULT_OK, resultIntent);
        Toast.makeText(this, "Subject saved", Toast.LENGTH_SHORT).show();
        finish();
    }
}