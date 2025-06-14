package com.example.luxevista;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog; // Import the TimePickerDialog
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.Locale; // Import Locale for string formatting

public class InHouseServiceBookingActivity extends AppCompatActivity {

    private TextView serviceNameText, selectedDateTimeText;
    private Button selectDateButton, selectTimeButton, confirmBookingButton;
    private DBHelper dbHelper;
    private ServiceItem currentService;
    private String selectedDate, selectedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_house_service_booking);

        dbHelper = new DBHelper(this);
        serviceNameText = findViewById(R.id.service_booking_name);
        selectedDateTimeText = findViewById(R.id.selected_date_time_text);
        selectDateButton = findViewById(R.id.select_date_button);
        selectTimeButton = findViewById(R.id.select_time_button);
        confirmBookingButton = findViewById(R.id.confirm_service_booking_button);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("SERVICE_DETAIL")) {
            currentService = (ServiceItem) intent.getSerializableExtra("SERVICE_DETAIL");
            if (currentService != null) {
                serviceNameText.setText(currentService.getName());
            }
        }

        selectDateButton.setOnClickListener(v -> showDatePicker());

        // --- THIS IS THE REVERTED CHANGE ---
        // The button now calls the clock-style time picker
        selectTimeButton.setOnClickListener(v -> showTimePicker());

        confirmBookingButton.setOnClickListener(v -> {
            if (currentService != null && selectedDate != null && selectedTime != null) {
                dbHelper.addServiceBooking(currentService, selectedDate, selectedTime);
                Toast.makeText(this, "Appointment for " + currentService.getName() + " booked!", Toast.LENGTH_LONG).show();
                finish();
            } else {
                Toast.makeText(this, "Please select both a date and time.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            selectedDate = year + "-" + (month + 1) + "-" + dayOfMonth;
            updateDateTimeText();
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    /**
     * THIS IS THE ORIGINAL METHOD that shows a clock to pick any time.
     */
    private void showTimePicker() {
        Calendar calendar = Calendar.getInstance();
        // The true parameter at the end sets it to a 24-hour format
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view, hourOfDay, minute) -> {
            selectedTime = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute);
            updateDateTimeText();
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
        timePickerDialog.show();
    }

    private void updateDateTimeText() {
        if (selectedDate != null && selectedTime != null) {
            selectedDateTimeText.setText("Selected: " + selectedDate + " at " + selectedTime);
            confirmBookingButton.setEnabled(true);
        } else if (selectedDate != null) {
            selectedDateTimeText.setText("Selected Date: " + selectedDate);
        }
    }
}
