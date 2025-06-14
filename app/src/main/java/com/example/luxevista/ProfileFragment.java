package com.example.luxevista;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Manages the user's profile screen. It displays user information and provides
 * menu options for editing the profile, viewing history, setting preferences,
 * and logging out.
 */
public class ProfileFragment extends Fragment {

    // UI Components
    private TextView profileName, profileEmail, travelDatesText;
    private CardView cardEditProfile, cardBookingHistory, cardTravelDates, cardPromotions, cardAttractions;
    private Button logoutButton;
    // Database helper
    private DBHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the XML layout for this fragment.
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize the database helper and link all UI components.
        dbHelper = new DBHelper(getContext());
        profileName = view.findViewById(R.id.profile_name);
        profileEmail = view.findViewById(R.id.profile_email);
        travelDatesText = view.findViewById(R.id.travel_dates_text);
        cardEditProfile = view.findViewById(R.id.card_edit_profile);
        cardBookingHistory = view.findViewById(R.id.card_booking_history);
        cardTravelDates = view.findViewById(R.id.card_travel_dates);
        cardPromotions = view.findViewById(R.id.card_promotions);
        cardAttractions = view.findViewById(R.id.card_attractions);
        logoutButton = view.findViewById(R.id.logout_button);

        // Attach listeners to all clickable elements.
        setupClickListeners();
    }

    /**
     * Called every time the fragment becomes visible. This ensures that if the user
     * edits their profile, the changes are reflected immediately upon returning to this screen.
     */
    @Override
    public void onResume() {
        super.onResume();
        loadUserProfile();
        loadTravelDates();
    }

    /**
     * Fetches the current user's data and updates the UI.
     */
    private void loadUserProfile() {
        if (getActivity() == null) return;
        // Get the logged-in user's email from SharedPreferences.
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String email = sharedPreferences.getString("userEmail", null);

        if (email != null) {
            // Use the email to get the user's name from the database.
            String name = dbHelper.getUserName(email);
            profileEmail.setText(email);
            if (name != null) {
                profileName.setText(name);
            }
        }
    }

    /**
     * Loads and displays the user's saved preferred travel dates from SharedPreferences.
     */
    private void loadTravelDates() {
        if (getActivity() == null) return;
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String checkIn = sharedPreferences.getString("prefCheckIn", null);
        String checkOut = sharedPreferences.getString("prefCheckOut", null);

        if (checkIn != null && checkOut != null) {
            travelDatesText.setText("Preferred Dates: " + checkIn + " to " + checkOut);
        } else {
            travelDatesText.setText("Not set. Tap to select.");
        }
    }

    /**
     * Sets up OnClickListeners for all interactive cards and buttons on the screen.
     */
    private void setupClickListeners() {
        // Navigate to EditProfileActivity.
        cardEditProfile.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), EditProfileActivity.class));
        });

        // Navigate to the "Bookings" tab.
        cardBookingHistory.setOnClickListener(v -> {
            if (getActivity() != null) {
                BottomNavigationView bottomNav = getActivity().findViewById(R.id.bottom_navigation);
                if (bottomNav != null) {
                    bottomNav.setSelectedItemId(R.id.nav_bookings);
                }
            }
        });

        // Show the date picker for travel dates.
        cardTravelDates.setOnClickListener(v -> showCheckInDatePicker());

        // Navigate to PromotionsActivity.
        cardPromotions.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), PromotionsActivity.class));
        });

        // Navigate to AttractionsActivity.
        cardAttractions.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), AttractionsActivity.class));
        });

        // Handle the logout process.
        logoutButton.setOnClickListener(v -> {
            if (getActivity() != null) {
                // Clear the user session from SharedPreferences.
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
                sharedPreferences.edit().clear().apply();

                // Navigate back to the LoginActivity and clear all previous screens.
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                Toast.makeText(getActivity(), "You have been logged out.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Displays a DatePickerDialog for the user to select a check-in date for their preferences.
     */
    private void showCheckInDatePicker() {
        if (getContext() == null) return;
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), (view, year, month, dayOfMonth) -> {
            String checkInDate = year + "-" + (month + 1) + "-" + dayOfMonth;
            // After selecting a start date, immediately show the picker for the end date.
            showCheckOutDatePicker(checkInDate);
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    /**
     * Displays a DatePickerDialog for the check-out date, ensuring it is after the check-in date.
     * @param checkInDate The selected check-in date, used to set a minimum for the check-out.
     */
    private void showCheckOutDatePicker(String checkInDate) {
        if (getContext() == null) return;
        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        long minDate = 0;
        try {
            Date date = sdf.parse(checkInDate);
            if (date != null) {
                minDate = date.getTime();
            }
        } catch (ParseException e) {
            e.printStackTrace();
            minDate = System.currentTimeMillis();
        }

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), (view, year, month, dayOfMonth) -> {
            String checkOutDate = year + "-" + (month + 1) + "-" + dayOfMonth;

            // Save the selected date range to SharedPreferences.
            if (getActivity() != null) {
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("prefCheckIn", checkInDate);
                editor.putString("prefCheckOut", checkOutDate);
                editor.apply();
                // Refresh the display to show the newly saved dates.
                loadTravelDates();
            }

        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        // Enforce that the check-out date cannot be earlier than the check-in date.
        datePickerDialog.getDatePicker().setMinDate(minDate);
        datePickerDialog.show();
    }
}
