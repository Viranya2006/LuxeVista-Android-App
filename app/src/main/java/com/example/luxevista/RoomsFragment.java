package com.example.luxevista;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
// Make sure to import the correct SearchView from androidx.appcompat
import androidx.appcompat.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RoomsFragment extends Fragment {

    // Declare all UI components and helpers
    private RecyclerView roomsRecyclerView;
    private RoomAdapter roomAdapter;
    private List<Room> roomList;
    private DBHelper dbHelper;
    private SearchView searchView;
    private Spinner filterSpinner, sortSpinner;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_rooms, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize DBHelper and RecyclerView
        dbHelper = new DBHelper(getContext());
        roomsRecyclerView = view.findViewById(R.id.rooms_recycler_view);
        roomsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize the new UI controls
        searchView = view.findViewById(R.id.search_view);
        filterSpinner = view.findViewById(R.id.filter_spinner);
        sortSpinner = view.findViewById(R.id.sort_spinner);

        // Setup the spinners with data from arrays.xml
        setupSpinners();

        // Setup the listeners for all controls
        setupListeners();

        // Perform an initial load of all rooms
        updateRoomList();
    }

    private void setupSpinners() {
        // Create an ArrayAdapter for the filter spinner
        ArrayAdapter<CharSequence> filterAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.room_types_array, android.R.layout.simple_spinner_item);
        filterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        filterSpinner.setAdapter(filterAdapter);

        // Create an ArrayAdapter for the sort spinner
        ArrayAdapter<CharSequence> sortAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.sort_options_array, android.R.layout.simple_spinner_item);
        sortAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortSpinner.setAdapter(sortAdapter);
    }

    /**
     * This method sets up the listeners for the search bar and spinners.
     * This is where the fix is.
     */
    private void setupListeners() {
        // Listener for the search view that triggers on every text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // This is called when the user presses the search button on the keyboard.
                updateRoomList();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // This is called every time the user types a character.
                updateRoomList();
                return true;
            }
        });

        // Listener for the filter spinner
        filterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateRoomList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // Listener for the sort spinner
        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateRoomList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    /**
     * This master method gets the current state of all UI controls,
     * queries the database, and updates the RecyclerView.
     */
    private void updateRoomList() {
        if (getContext() == null) return; // Prevents crashes if fragment is detached

        // Get current values from UI controls
        String searchQuery = searchView.getQuery().toString();
        String roomTypeFilter = filterSpinner.getSelectedItem().toString();
        String sortOrder = sortSpinner.getSelectedItem().toString();

        // Get the filtered and sorted list from the database
        roomList = dbHelper.getFilteredAndSortedRooms(roomTypeFilter, searchQuery, sortOrder);

        // Update the adapter
        if (roomAdapter == null) {
            // If it's the first time, create a new adapter
            roomAdapter = new RoomAdapter(roomList);
            roomsRecyclerView.setAdapter(roomAdapter);
        } else {
            // Otherwise, just update the list inside the existing adapter
            roomAdapter.filterList(roomList);
        }
    }
}
