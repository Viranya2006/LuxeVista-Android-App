package com.example.luxevista;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.Locale;

/**
 * Handles the booking process for a specific in-house service.
 * This activity receives a ServiceItem object, allows the user to select
 * a date and time, and saves the reservation to the database.
 */
public class InHouseServiceBookingActivity extends AppCompatActivity {

    // UI Elements
    private TextView serviceNameText, selectedDateTimeText;
    private Button selectDateButton, selectTimeButton, confirmBookingButton;

    // Data and Helpers
    private DBHelper dbHelper;
    private ServiceItem currentService;
    private String selectedDate, selectedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_house_service_booking);

        // Initialize helpers and UI components.
        dbHelper = new DBHelper(this);
        serviceNameText = findViewById(R.id.service_booking_name);
        selectedDateTimeText = findViewById(R.id.selected_date_time_text);
        selectDateButton = findViewById(R.id.select_date_button);
        selectTimeButton = findViewById(R.id.select_time_button);
        confirmBookingButton = findViewById(R.id.confirm_service_booking_button);

        // Get the ServiceItem object passed from the ServicesFragment.
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("SERVICE_DETAIL")) {
            currentService = (ServiceItem) intent.getSerializableExtra("SERVICE_DETAIL");
            if (currentService != null) {
                serviceNameText.setText(currentService.getName());
            }
        }

        // Set up click listeners for the buttons.
        selectDateButton.setOnClickListener(v -> showDatePicker());
        selectTimeButton.setOnClickListener(v -> showTimePicker());
        confirmBookingButton.setOnClickListener(v -> confirmReservation());
    }

    /**
     * Displays a standard DatePickerDialog to allow the user to select a date.
     */
    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            selectedDate = year + "-" + (month + 1) + "-" + dayOfMonth;
            updateDateTimeText();
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000); // Prevent past dates
        datePickerDialog.show();
    }

    /**
     * Displays a standard TimePickerDialog (clock) for the user to select a time.
     */
    private void showTimePicker() {
        Calendar calendar = Calendar.getInstance();
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view, hourOfDay, minute) -> {
            // Format the time to ensure it has leading zeros (e.g., 09:05).
            selectedTime = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute);
            updateDateTimeText();
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true); // true for 24-hour format
        timePickerDialog.show();
    }

    /**
     * Updates the display text to show the currently selected date and time.
     * Also enables the confirm button once both are selected.
     */
    private void updateDateTimeText() {
        if (selectedDate != null && selectedTime != null) {
            selectedDateTimeText.setText("Selected: " + selectedDate + " at " + selectedTime);
            confirmBookingButton.setEnabled(true);
        } else if (selectedDate != null) {
            selectedDateTimeText.setText("Selected Date: " + selectedDate);
        }
    }

    /**
     * Finalizes the reservation by adding it to the database.
     */
    private void confirmReservation() {
        if (currentService != null && selectedDate != null && selectedTime != null) {
            dbHelper.addServiceBooking(currentService, selectedDate, selectedTime);
            Toast.makeText(this, "Appointment for " + currentService.getName() + " reserved!", Toast.LENGTH_LONG).show();
            finish(); // Close activity and return.
        } else {
            Toast.makeText(this, "Please select both a date and time.", Toast.LENGTH_SHORT).show();
        }
    }
}
