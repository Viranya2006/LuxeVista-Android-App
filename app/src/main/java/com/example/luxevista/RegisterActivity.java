package com.example.luxevista;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Handles the user registration process. This activity provides a form for new users
 * to create an account, validates the input, and saves the new user to the database.
 */
public class RegisterActivity extends AppCompatActivity {

    // UI elements from the activity_register.xml layout.
    private EditText editTextFullName, editTextContact, editTextEmail, editTextPassword, editTextConfirmPassword;
    private Button buttonRegister;
    private TextView textViewLogin;
    // Database helper instance to interact with the SQLite database.
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize the database helper.
        dbHelper = new DBHelper(this);

        // Link UI element variables to their corresponding views in the XML layout.
        editTextFullName = findViewById(R.id.editTextFullName);
        editTextContact = findViewById(R.id.editTextContact);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);
        buttonRegister = findViewById(R.id.buttonRegister);
        textViewLogin = findViewById(R.id.textViewLogin);

        // Set a click listener for the main "Register" button.
        buttonRegister.setOnClickListener(v -> registerUser());

        // Set a click listener for the "Login" text, which returns the user to the login screen.
        textViewLogin.setOnClickListener(v -> finish());
    }

    /**
     * This method orchestrates the user registration process when the "Register" button is clicked.
     */
    private void registerUser() {
        // Retrieve the user's input from the EditText fields.
        String name = editTextFullName.getText().toString().trim();
        String contact = editTextContact.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String confirmPassword = editTextConfirmPassword.getText().toString().trim();

        // --- Step 1: Input Validation ---

        // Check if any of the fields are empty.
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(contact) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return; // Stop the registration process if validation fails.
        }

        // Validate that the contact number is exactly 10 digits.
        if (contact.length() != 10) {
            Toast.makeText(this, "Contact number must be 10 digits", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validate that the password meets the required complexity rules.
        if (!isValidPassword(password)) {
            Toast.makeText(this, "Password must contain at least 3 letters, 3 numbers, and 1 special character", Toast.LENGTH_LONG).show();
            return;
        }

        // Validate that the entered passwords match.
        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        // --- Step 2: Add User to Database ---

        // Attempt to add the new user to the SQLite database.
        boolean isAdded = dbHelper.addUser(name, email, password, contact);

        if (isAdded) {
            // If the database insertion was successful, inform the user and close this screen.
            Toast.makeText(this, "Registration successful! Please login.", Toast.LENGTH_LONG).show();
            finish(); // Close RegisterActivity to return to the LoginActivity.
        } else {
            // If insertion failed (e.g., the email is already in use), inform the user.
            Toast.makeText(this, "Registration failed. Email may already be in use.", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Validates password complexity using regular expressions.
     * Rules: At least 3 letters, 3 numbers, and 1 special character.
     * @param password The password string to validate.
     * @return true if the password is valid, false otherwise.
     */
    public boolean isValidPassword(String password) {
        // Regular expressions to check for letters, digits, and special characters.
        Pattern letter = Pattern.compile("[a-zA-Z]");
        Pattern digit = Pattern.compile("[0-9]");
        Pattern special = Pattern.compile("[!@#$%^&*()_+=|<>?{}\\[\\]~-]");

        Matcher hasLetter = letter.matcher(password);
        Matcher hasDigit = digit.matcher(password);
        Matcher hasSpecial = special.matcher(password);

        int letterCount = 0;
        while (hasLetter.find()) {
            letterCount++;
        }

        int digitCount = 0;
        while (hasDigit.find()) {
            digitCount++;
        }

        // Check if at least one special character exists.
        boolean hasSpecialChar = hasSpecial.find();

        return letterCount >= 3 && digitCount >= 3 && hasSpecialChar;
    }
}
