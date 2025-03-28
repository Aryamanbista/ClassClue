package com.example.classclue;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText emailEditText, passwordEditText;
    private Spinner userTypeSpinner;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Bind UI elements
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        userTypeSpinner = findViewById(R.id.userTypeSpinner);
        loginButton = findViewById(R.id.loginButton);

        // Set login button click listener
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();
                String userType = userTypeSpinner.getSelectedItem().toString();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(Login.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                } else {
                    loginUser(email, password, userType);
                }
            }
        });
    }

    private void loginUser(String email, String password, String userType) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(Login.this, "Login successful as " + userType, Toast.LENGTH_SHORT).show();
                        // Redirect based on user type
                        if (userType.equals("Admin")) {
                            startActivity(new Intent(Login.this, AdminDashboard.class));
                        } else {
                            startActivity(new Intent(Login.this, StudentDashboard.class));
                        }
                        finish(); // Close login activity
                    } else {
                        Toast.makeText(Login.this, "Login failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}