package com.example.luxevista;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView featuredRoomsRecyclerView;
    private RoomAdapter roomAdapter;
    private DBHelper dbHelper;
    private CardView cardBookRoom, cardSpa;
    private TextView welcomeText; // TextView for the welcome message

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // --- INITIALIZE VIEWS AND HELPERS ---
        dbHelper = new DBHelper(getContext());
        welcomeText = view.findViewById(R.id.welcome_text); // Find the welcome text view
        cardBookRoom = view.findViewById(R.id.card_book_room);
        cardSpa = view.findViewById(R.id.card_spa);
        featuredRoomsRecyclerView = view.findViewById(R.id.featured_rooms_recycler_view);
        featuredRoomsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        featuredRoomsRecyclerView.setNestedScrollingEnabled(false);

        // --- LOAD USER AND ROOM DATA ---
        loadUserProfile();
        loadFeaturedRooms();

        // --- SETUP CLICK LISTENERS ---
        setupClickListeners();
    }

    private void loadUserProfile() {
        // Retrieve the saved email from SharedPreferences, which we saved during login
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String email = sharedPreferences.getString("userEmail", null);

        if (email != null) {
            // Use the email to get the user's name from the database
            String name = dbHelper.getUserName(email);
            if (name != null) {
                // Set the personalized welcome message
                welcomeText.setText("Welcome, " + name);
            } else {
                welcomeText.setText("Welcome, Guest");
            }
        } else {
            welcomeText.setText("Welcome, Guest");
        }
    }

    private void loadFeaturedRooms() {
        List<Room> allRooms = dbHelper.getAllRooms();
        if (allRooms != null && !allRooms.isEmpty()) {
            // Get only the first 2 rooms for the featured list
            List<Room> featuredRooms = allRooms.subList(0, Math.min(allRooms.size(), 2));
            roomAdapter = new RoomAdapter(featuredRooms);
            featuredRoomsRecyclerView.setAdapter(roomAdapter);
        }
    }

    private void setupClickListeners() {
        // Set click listener for Book a Room
        cardBookRoom.setOnClickListener(v -> {
            BottomNavigationView bottomNav = getActivity().findViewById(R.id.bottom_navigation);
            if (bottomNav != null) {
                bottomNav.setSelectedItemId(R.id.nav_rooms);
            }
        });

        // Set click listener for In-House Services
        cardSpa.setOnClickListener(v -> {
            if (getActivity() != null) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, new ServicesFragment());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
    }
}
