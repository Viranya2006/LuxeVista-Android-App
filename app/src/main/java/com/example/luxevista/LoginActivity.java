package com.example.luxevista;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Handles the user login process. This activity validates entered credentials
 * against the database. On successful login, it saves the user's session
 * and navigates to the main part of the application.
 */
public class LoginActivity extends AppCompatActivity {

    // UI elements and database helper.
    private EditText editTextEmail, editTextPassword;
    private Button buttonLogin;
    private TextView textViewRegister;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize the database helper.
        dbHelper = new DBHelper(this);

        // Link UI elements to their views in the XML layout.
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        textViewRegister = findViewById(R.id.textViewRegister);

        // Set the click listener for the "Login" button.
        buttonLogin.setOnClickListener(v -> loginUser());

        // Set the click listener for the "Register" text, which navigates to the RegisterActivity.
        textViewRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    /**
     * This method handles the logic when the user clicks the "Login" button.
     */
    private void loginUser() {
        // Retrieve input from the EditText fields.
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        // --- Step 1: Input Validation ---
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Email and Password are required", Toast.LENGTH_SHORT).show();
            return;
        }

        // --- Step 2: Authenticate User ---
        // Check if a user with the given credentials exists in the database.
        boolean userExists = dbHelper.checkUser(email, password);

        if (userExists) {
            // If login is successful, provide feedback.
            Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show();

            // --- Step 3: Save User Session ---
            // We use SharedPreferences to store the logged-in user's email,
            // so other parts of the app can know who is logged in.
            SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("userEmail", email);
            editor.apply(); // Asynchronously save the changes.

            // --- Step 4: Navigate to the Main Activity ---
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish(); // Close LoginActivity so the user can't go back to it.
        } else {
            // If login fails, inform the user.
            Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show();
        }
    }
}
