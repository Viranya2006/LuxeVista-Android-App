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

public class RegisterActivity extends AppCompatActivity {

    private EditText editTextFullName, editTextContact, editTextEmail, editTextPassword, editTextConfirmPassword;
    private Button buttonRegister;
    private TextView textViewLogin;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        dbHelper = new DBHelper(this);
        editTextFullName = findViewById(R.id.editTextFullName);
        editTextContact = findViewById(R.id.editTextContact);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);
        buttonRegister = findViewById(R.id.buttonRegister);
        textViewLogin = findViewById(R.id.textViewLogin);

        buttonRegister.setOnClickListener(v -> registerUser());
        textViewLogin.setOnClickListener(v -> finish());
    }

    private void registerUser() {
        String name = editTextFullName.getText().toString().trim();
        String contact = editTextContact.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String confirmPassword = editTextConfirmPassword.getText().toString().trim();

        // --- Input Validation ---
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(contact) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        if (contact.length() != 10) {
            Toast.makeText(this, "Contact number must be 10 digits", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isValidPassword(password)) {
            Toast.makeText(this, "Password must contain at least 3 letters, 3 numbers, and 1 special character", Toast.LENGTH_LONG).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        // --- Add user to database ---
        boolean isAdded = dbHelper.addUser(name, email, password, contact);

        if (isAdded) {
            Toast.makeText(this, "Registration successful! Please login.", Toast.LENGTH_LONG).show();
            finish();
        } else {
            Toast.makeText(this, "Registration failed. Email may already be in use.", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Validates password complexity.
     * Rules: At least 3 letters, 3 numbers, 1 special character.
     */
    public boolean isValidPassword(String password) {
        // Regex to check for at least 3 letters, 3 numbers, and 1 special character
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

        int specialCount = 0;
        while (hasSpecial.find()) {
            specialCount++;
        }

        return letterCount >= 3 && digitCount >= 3 && specialCount >= 1;
    }
}
