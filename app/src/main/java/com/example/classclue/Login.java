package com.example.classclue;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private TextInputEditText emailEditText, passwordEditText;
    private MaterialAutoCompleteTextView userTypeSpinner;
    private MaterialButton loginButton;
    private TextInputLayout emailInputLayout, passwordInputLayout, userTypeInputLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Bind UI elements
        initializeViews();

        // Setup user type spinner
        setupUserTypeSpinner();

        // Set login button click listener
        loginButton.setOnClickListener(v -> validateAndLogin());
    }

    private void initializeViews() {
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        userTypeSpinner = findViewById(R.id.userTypeSpinner);
        loginButton = findViewById(R.id.loginButton);

        emailInputLayout = findViewById(R.id.emailInputLayout);
        passwordInputLayout = findViewById(R.id.passwordInputLayout);
        userTypeInputLayout = findViewById(R.id.userTypeInputLayout);
    }

    private void setupUserTypeSpinner() {
        String[] userTypes = {"Student", "Admin"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                userTypes
        );
        userTypeSpinner.setAdapter(adapter);
    }

    private void validateAndLogin() {
        // Reset previous errors
        emailInputLayout.setError(null);
        passwordInputLayout.setError(null);
        userTypeInputLayout.setError(null);

        // Get input values
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String userType = userTypeSpinner.getText().toString();

        // Validate inputs
        if (!validateEmail(email)) return;
        if (!validatePassword(password)) return;
        if (TextUtils.isEmpty(userType)) {
            userTypeInputLayout.setError("Please select a user type");
            return;
        }

        // Attempt login
        loginUser(email, password, userType);
    }

    private boolean validateEmail(String email) {
        if (TextUtils.isEmpty(email)) {
            emailInputLayout.setError("Email cannot be empty");
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailInputLayout.setError("Invalid email format");
            return false;
        }
        return true;
    }

    private boolean validatePassword(String password) {
        if (TextUtils.isEmpty(password)) {
            passwordInputLayout.setError("Password cannot be empty");
            return false;
        }
        if (password.length() < 6) {
            passwordInputLayout.setError("Password must be at least 6 characters");
            return false;
        }
        return true;
    }

    private void loginUser(String email, String password, String userType) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(Login.this, "Login successful as " + userType, Toast.LENGTH_SHORT).show();

                        // Redirect based on user type
                        Intent intent;
                        if (userType.equals("Admin")) {
                            intent = new Intent(Login.this, AdminDashboard.class);
                        } else {
                            intent = new Intent(Login.this, StudentDashboard.class);
                        }
                        startActivity(intent);
                        finish(); // Close login activity
                    } else {
                        Toast.makeText(Login.this,
                                "Login failed: " + task.getException().getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is already logged in
        if (mAuth.getCurrentUser() != null) {
            // Redirect to dashboard (you might want to add logic to determine user type)
            startActivity(new Intent(Login.this, StudentDashboard.class));
            finish();
        }
    }
}