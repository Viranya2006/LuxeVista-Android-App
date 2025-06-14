package com.example.luxevista;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Displays the detailed view for a single hotel room.
 * This activity receives a Room object via an Intent and populates the UI with its details.
 * It also handles the user interaction for selecting booking dates and confirming the booking.
 */
public class RoomDetailActivity extends AppCompatActivity {

    // UI elements from the layout
    private ImageView roomImage;
    private TextView roomName, roomPrice, roomDescription, checkInDateText, checkOutDateText;
    private Button selectDatesButton, confirmBookingButton;

    // Data and Helpers
    private DBHelper dbHelper;
    private Room currentRoom;
    private String checkInDate, checkOutDate;
    private long checkInDateMillis = 0; // Used to validate check-out date

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_detail);

        // Initialize the database helper and link UI components.
        dbHelper = new DBHelper(this);
        roomImage = findViewById(R.id.detail_room_image);
        roomName = findViewById(R.id.detail_room_name);
        roomPrice = findViewById(R.id.detail_room_price);
        roomDescription = findViewById(R.id.detail_room_description);
        checkInDateText = findViewById(R.id.check_in_date_text);
        checkOutDateText = findViewById(R.id.check_out_date_text);
        selectDatesButton = findViewById(R.id.select_dates_button);
        confirmBookingButton = findViewById(R.id.confirm_booking_button);

        // Retrieve the Room object passed from the previous screen.
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("ROOM_DETAIL")) {
            currentRoom = (Room) intent.getSerializableExtra("ROOM_DETAIL");
            // If the room data is valid, populate the UI.
            if (currentRoom != null) {
                populateUI(currentRoom);
            }
        }

        // Set up click listeners for the buttons.
        selectDatesButton.setOnClickListener(v -> showCheckInDatePicker());
        confirmBookingButton.setOnClickListener(v -> confirmBooking());
    }

    /**
     * Populates the UI elements with data from the passed Room object.
     * @param room The Room object to display.
     */
    private void populateUI(Room room) {
        roomImage.setImageResource(room.getImageResId());
        roomName.setText(room.getName());
        roomPrice.setText(room.getPrice());
        roomDescription.setText(room.getDescription());
    }

    /**
     * Displays a DatePickerDialog for the user to select a check-in date.
     */
    private void showCheckInDatePicker() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            // Format the selected date and store it.
            checkInDate = year + "-" + (month + 1) + "-" + dayOfMonth;
            checkInDateText.setText("Check-in: " + checkInDate);
            // After setting check-in, automatically prompt for check-out.
            showCheckOutDatePicker();
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        // Prevent users from selecting a date in the past.
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    /**
     * Displays a DatePickerDialog for the user to select a check-out date.
     * It ensures the check-out date is after the check-in date.
     */
    private void showCheckOutDatePicker() {
        Calendar calendar = Calendar.getInstance();
        // Parse the check-in date to set a minimum date for the check-out calendar.
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            Date date = sdf.parse(checkInDate);
            if (date != null) {
                checkInDateMillis = date.getTime();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            checkOutDate = year + "-" + (month + 1) + "-" + dayOfMonth;
            checkOutDateText.setText("Check-out: " + checkOutDate);
            // Enable the confirm button now that both dates are selected.
            confirmBookingButton.setEnabled(true);
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        // Set the minimum selectable date to be the day after check-in.
        datePickerDialog.getDatePicker().setMinDate(checkInDateMillis + (24 * 60 * 60 * 1000)); // Add one day in milliseconds
        datePickerDialog.show();
    }

    /**
     * Handles the final booking confirmation. Validates that all necessary data is present
     * and then adds the booking to the database.
     */
    private void confirmBooking() {
        if (currentRoom != null && checkInDate != null && checkOutDate != null) {
            // Add the booking to the database.
            dbHelper.addBooking(currentRoom, checkInDate, checkOutDate);
            Toast.makeText(this, "Room booked successfully!", Toast.LENGTH_LONG).show();
            // Close this activity and return to the previous screen.
            finish();
        } else {
            // Show an error if dates are not selected.
            Toast.makeText(this, "Please select both check-in and check-out dates.", Toast.LENGTH_SHORT).show();
        }
    }
}
