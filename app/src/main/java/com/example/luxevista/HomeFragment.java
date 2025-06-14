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

/**
 * The HomeFragment acts as the main landing screen of the application after login.
 * It displays a personalized welcome message, provides quick access to other sections
 * like Rooms and Services, and showcases a list of featured rooms.
 */
public class HomeFragment extends Fragment {

    // UI Components
    private RecyclerView featuredRoomsRecyclerView;
    private CardView cardBookRoom, cardServices;
    private TextView welcomeText;

    // Data and Helpers
    private RoomAdapter roomAdapter;
    private DBHelper dbHelper;

    /**
     * Called to have the fragment instantiate its user interface view.
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflates the XML layout file for this fragment.
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    /**
     * Called immediately after onCreateView has returned, but before any saved state has been restored into the view.
     * This is where we initialize the UI and set up functionality.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize the database helper and link the UI components to the layout.
        dbHelper = new DBHelper(getContext());
        welcomeText = view.findViewById(R.id.welcome_text);
        cardBookRoom = view.findViewById(R.id.card_book_room);
        cardServices = view.findViewById(R.id.card_spa); // Note: ID from XML is card_spa
        featuredRoomsRecyclerView = view.findViewById(R.id.featured_rooms_recycler_view);
        featuredRoomsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        featuredRoomsRecyclerView.setNestedScrollingEnabled(false); // Improves scrolling within the ScrollView.

        // Load dynamic content into the fragment.
        loadUserProfile();
        loadFeaturedRooms();
        setupClickListeners();
    }

    /**
     * Loads the logged-in user's name from the database and updates the welcome message.
     */
    private void loadUserProfile() {
        // Access SharedPreferences to get the email of the logged-in user.
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String email = sharedPreferences.getString("userEmail", null);

        if (email != null) {
            // Fetch the user's name from the database using their email.
            String name = dbHelper.getUserName(email);
            // Set the personalized welcome message.
            if (name != null) {
                welcomeText.setText("Welcome, " + name);
            } else {
                welcomeText.setText("Welcome, Guest");
            }
        }
    }

    /**
     * Fetches all rooms from the database and displays the first two as "Featured Rooms".
     */
    private void loadFeaturedRooms() {
        List<Room> allRooms = dbHelper.getAllRooms();
        if (allRooms != null && !allRooms.isEmpty()) {
            // Create a sublist containing up to the first two rooms.
            List<Room> featuredRooms = allRooms.subList(0, Math.min(allRooms.size(), 2));
            // Initialize the adapter and set it on the RecyclerView.
            roomAdapter = new RoomAdapter(featuredRooms);
            featuredRoomsRecyclerView.setAdapter(roomAdapter);
        }
    }

    /**
     * Sets up the OnClickListeners for the interactive cards on the home screen.
     */
    private void setupClickListeners() {
        // Listener for the "Book a Room" card.
        cardBookRoom.setOnClickListener(v -> {
            // Programmatically selects the "Rooms" tab in the main BottomNavigationView.
            BottomNavigationView bottomNav = getActivity().findViewById(R.id.bottom_navigation);
            if (bottomNav != null) {
                bottomNav.setSelectedItemId(R.id.nav_rooms);
            }
        });

        // Listener for the "In-House Services" card.
        cardServices.setOnClickListener(v -> {
            // Replaces the current fragment with the ServicesFragment.
            if (getActivity() != null) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, new ServicesFragment());
                fragmentTransaction.addToBackStack(null); // Allows the user to press 'back' to return here.
                fragmentTransaction.commit();
            }
        });
    }
}
