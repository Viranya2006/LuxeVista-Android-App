package com.example.luxevista;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class RoomDetailActivity extends AppCompatActivity {

    private ImageView roomImage;
    private TextView roomName, roomPrice, roomDescription, checkInDateText, checkOutDateText;
    private Button selectDatesButton, confirmBookingButton;

    private DBHelper dbHelper;
    private Room currentRoom;
    private String checkInDate, checkOutDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_detail);

        dbHelper = new DBHelper(this);
        // Find all views
        roomImage = findViewById(R.id.detail_room_image);
        roomName = findViewById(R.id.detail_room_name);
        roomPrice = findViewById(R.id.detail_room_price);
        roomDescription = findViewById(R.id.detail_room_description);
        checkInDateText = findViewById(R.id.check_in_date_text);
        checkOutDateText = findViewById(R.id.check_out_date_text);
        selectDatesButton = findViewById(R.id.select_dates_button);
        confirmBookingButton = findViewById(R.id.confirm_booking_button);

        // Get Room object from Intent
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("ROOM_DETAIL")) {
            currentRoom = (Room) intent.getSerializableExtra("ROOM_DETAIL");
            if (currentRoom != null) {
                populateUI(currentRoom);
            }
        }

        // Set click listener for selecting dates
        selectDatesButton.setOnClickListener(v -> showCheckInDatePicker());

        // Set click listener for confirming the booking
        confirmBookingButton.setOnClickListener(v -> {
            if (currentRoom != null && checkInDate != null && checkOutDate != null) {
                dbHelper.addBooking(currentRoom, checkInDate, checkOutDate);
                Toast.makeText(this, "Room booked successfully!", Toast.LENGTH_LONG).show();
                finish();
            } else {
                Toast.makeText(this, "Please select both check-in and check-out dates.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showCheckInDatePicker() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            checkInDate = year + "-" + (month + 1) + "-" + dayOfMonth;
            checkInDateText.setText("Check-in: " + checkInDate);
            // After selecting check-in, automatically show check-out picker
            showCheckOutDatePicker();
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000); // User can't select past dates
        datePickerDialog.show();
    }

    private void showCheckOutDatePicker() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            checkOutDate = year + "-" + (month + 1) + "-" + dayOfMonth;
            checkOutDateText.setText("Check-out: " + checkOutDate);
            // Enable the confirm button once both dates are set
            confirmBookingButton.setEnabled(true);
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    private void populateUI(Room room) {
        roomImage.setImageResource(room.getImageResId());
        roomName.setText(room.getName());
        roomPrice.setText(room.getPrice());
        roomDescription.setText(room.getDescription());
    }
}