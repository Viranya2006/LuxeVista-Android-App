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

public class ProfileFragment extends Fragment {

    private TextView profileName, profileEmail, travelDatesText;
    private CardView cardEditProfile, cardBookingHistory, cardTravelDates, cardPromotions, cardAttractions;
    private Button logoutButton;
    private DBHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dbHelper = new DBHelper(getContext());
        // Find all views by their IDs
        profileName = view.findViewById(R.id.profile_name);
        profileEmail = view.findViewById(R.id.profile_email);
        travelDatesText = view.findViewById(R.id.travel_dates_text);
        cardEditProfile = view.findViewById(R.id.card_edit_profile);
        cardBookingHistory = view.findViewById(R.id.card_booking_history);
        cardTravelDates = view.findViewById(R.id.card_travel_dates);
        cardPromotions = view.findViewById(R.id.card_promotions);
        cardAttractions = view.findViewById(R.id.card_attractions); // Find the new attractions card
        logoutButton = view.findViewById(R.id.logout_button);

        setupClickListeners();
    }

    @Override
    public void onResume() {
        super.onResume();
        // Load data every time the fragment is viewed to reflect changes from EditProfileActivity
        loadUserProfile();
        loadTravelDates();
    }

    private void loadUserProfile() {
        if (getActivity() == null) return;
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String email = sharedPreferences.getString("userEmail", null);

        if (email != null) {
            String name = dbHelper.getUserName(email);
            profileEmail.setText(email);
            if (name != null) {
                profileName.setText(name);
            } else {
                profileName.setText("Guest User");
            }
        }
    }

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

    private void setupClickListeners() {
        cardEditProfile.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), EditProfileActivity.class));
        });

        cardBookingHistory.setOnClickListener(v -> {
            if (getActivity() != null) {
                BottomNavigationView bottomNav = getActivity().findViewById(R.id.bottom_navigation);
                if (bottomNav != null) {
                    bottomNav.setSelectedItemId(R.id.nav_bookings);
                }
            }
        });

        cardTravelDates.setOnClickListener(v -> showCheckInDatePicker());

        cardPromotions.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), PromotionsActivity.class));
        });

        // Add the click listener for the new attractions card
        cardAttractions.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), AttractionsActivity.class));
        });

        logoutButton.setOnClickListener(v -> {
            if (getActivity() != null) {
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
                sharedPreferences.edit().clear().apply();

                Intent intent = new Intent(getActivity(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                Toast.makeText(getActivity(), "You have been logged out.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showCheckInDatePicker() {
        if (getContext() == null) return;
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), (view, year, month, dayOfMonth) -> {
            String checkInDate = year + "-" + (month + 1) + "-" + dayOfMonth;
            showCheckOutDatePicker(checkInDate);
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

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

            if (getActivity() != null) {
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("prefCheckIn", checkInDate);
                editor.putString("prefCheckOut", checkOutDate);
                editor.apply();
                loadTravelDates();
            }

        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.getDatePicker().setMinDate(minDate);
        datePickerDialog.show();
    }
}
