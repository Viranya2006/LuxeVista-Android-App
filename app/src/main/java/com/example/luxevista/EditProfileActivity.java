package com.example.luxevista;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.regex.Pattern;

public class EditProfileActivity extends AppCompatActivity {

    private EditText editTextName, editTextContact, editTextEmail, editTextPassword;
    private Button saveChangesButton;
    private DBHelper dbHelper;
    private String originalUserEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        dbHelper = new DBHelper(this);
        editTextName = findViewById(R.id.edit_text_name);
        editTextContact = findViewById(R.id.edit_text_contact);
        editTextEmail = findViewById(R.id.edit_text_email);
        editTextPassword = findViewById(R.id.edit_text_password);
        saveChangesButton = findViewById(R.id.save_changes_button);

        loadUserData();

        saveChangesButton.setOnClickListener(v -> saveUserData());
    }

    private void loadUserData() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        originalUserEmail = sharedPreferences.getString("userEmail", null);

        if (originalUserEmail != null) {
            Cursor cursor = dbHelper.getUserDetails(originalUserEmail);
            if (cursor != null && cursor.moveToFirst()) {
                int nameIndex = cursor.getColumnIndex(DBHelper.COLUMN_USER_NAME);
                int contactIndex = cursor.getColumnIndex(DBHelper.COLUMN_USER_CONTACT);
                int emailIndex = cursor.getColumnIndex(DBHelper.COLUMN_USER_EMAIL);

                if (nameIndex != -1) editTextName.setText(cursor.getString(nameIndex));
                if (contactIndex != -1) editTextContact.setText(cursor.getString(contactIndex));
                if (emailIndex != -1) editTextEmail.setText(cursor.getString(emailIndex));

                cursor.close();
            }
        }
    }

    private void saveUserData() {
        String newName = editTextName.getText().toString().trim();
        String newContact = editTextContact.getText().toString().trim();
        String newEmail = editTextEmail.getText().toString().trim();
        String newPassword = editTextPassword.getText().toString().trim();

        // Add validation
        if (newName.isEmpty() || newContact.isEmpty() || newEmail.isEmpty()) {
            Toast.makeText(this, "Name, Contact, and Email cannot be empty.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validate password only if it's being changed
        if (!newPassword.isEmpty() && !isValidPassword(newPassword)) {
            Toast.makeText(this, "Password must contain at least 3 letters, 3 numbers, and 1 special character", Toast.LENGTH_LONG).show();
            return;
        }

        boolean isUpdated = dbHelper.updateUser(originalUserEmail, newName, newContact, newEmail, newPassword);

        if (isUpdated) {
            // Update the email in SharedPreferences if it was changed
            SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("userEmail", newEmail);
            editor.apply();

            Toast.makeText(this, "Profile updated successfully!", Toast.LENGTH_SHORT).show();
            finish(); // Go back to the profile screen
        } else {
            Toast.makeText(this, "Failed to update profile.", Toast.LENGTH_SHORT).show();
        }
    }

    // Reuse the password validation logic
    public boolean isValidPassword(String password) {
        Pattern letter = Pattern.compile("[a-zA-Z]");
        Pattern digit = Pattern.compile("[0-9]");
        Pattern special = Pattern.compile("[!@#$%^&*()_+=|<>?{}\\[\\]~-]");
        // ... (rest of the validation logic)
        java.util.regex.Matcher hasLetter = letter.matcher(password);
        java.util.regex.Matcher hasDigit = digit.matcher(password);
        java.util.regex.Matcher hasSpecial = special.matcher(password);
        int letterCount = 0;
        while(hasLetter.find()) letterCount++;
        int digitCount = 0;
        while(hasDigit.find()) digitCount++;
        int specialCount = 0;
        while(hasSpecial.find()) specialCount++;

        return letterCount >= 3 && digitCount >= 3 && specialCount >= 1;
    }
}
