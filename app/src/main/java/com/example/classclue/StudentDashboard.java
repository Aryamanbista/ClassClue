package com.example.classclue;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.TextView;

public class StudentDashboard extends AppCompatActivity {
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_dashboard);

        TextView welcomeText = findViewById(R.id.welcomeText);
        welcomeText.setText("Welcome, Student!");
    }
}