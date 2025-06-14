package com.example.luxevista;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import androidx.appcompat.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * The RoomsFragment displays a complete list of all hotel rooms.
 * It includes UI controls for searching, filtering by room type, and sorting by price,
 * allowing users to easily find the accommodation that suits their needs.
 */
public class RoomsFragment extends Fragment {

    // UI Components
    private RecyclerView roomsRecyclerView;
    private SearchView searchView;
    private Spinner filterSpinner, sortSpinner;

    // Data and Helpers
    private RoomAdapter roomAdapter;
    private List<Room> roomList;
    private DBHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the XML layout for this fragment.
        return inflater.inflate(R.layout.fragment_rooms, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize the database helper and UI components.
        dbHelper = new DBHelper(getContext());
        roomsRecyclerView = view.findViewById(R.id.rooms_recycler_view);
        roomsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        searchView = view.findViewById(R.id.search_view);
        filterSpinner = view.findViewById(R.id.filter_spinner);
        sortSpinner = view.findViewById(R.id.sort_spinner);

        // Set up the dropdown menus and their listeners.
        setupSpinners();
        setupListeners();

        // Perform an initial load of the room list.
        updateRoomList();
    }

    /**
     * Populates the filter and sort Spinners (dropdowns) with data from arrays.xml.
     */
    private void setupSpinners() {
        if (getContext() == null) return;
        // Set up the adapter for the room type filter.
        ArrayAdapter<CharSequence> filterAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.room_types_array, android.R.layout.simple_spinner_item);
        filterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        filterSpinner.setAdapter(filterAdapter);

        // Set up the adapter for the price sort options.
        ArrayAdapter<CharSequence> sortAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.sort_options_array, android.R.layout.simple_spinner_item);
        sortAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortSpinner.setAdapter(sortAdapter);
    }

    /**
     * Attaches listeners to the search, filter, and sort controls.
     * Any change in these controls will trigger an update of the room list.
     */
    private void setupListeners() {
        // Listener for the search bar.
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                updateRoomList();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                updateRoomList();
                return true;
            }
        });

        // A single listener for both spinners.
        AdapterView.OnItemSelectedListener spinnerListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateRoomList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        };
        filterSpinner.setOnItemSelectedListener(spinnerListener);
        sortSpinner.setOnItemSelectedListener(spinnerListener);
    }

    /**
     * The master update method. It reads the current state of all filter/sort controls,
     * queries the database for the relevant data, and updates the RecyclerView.
     */
    private void updateRoomList() {
        if (getContext() == null) return; // Ensure fragment is still attached.

        // Get the current values from the search bar and spinners.
        String searchQuery = searchView.getQuery().toString();
        String roomTypeFilter = filterSpinner.getSelectedItem().toString();
        String sortOrder = sortSpinner.getSelectedItem().toString();

        // Fetch the filtered and sorted list from the database.
        roomList = dbHelper.getFilteredAndSortedRooms(roomTypeFilter, searchQuery, sortOrder);

        // Update the adapter with the new list.
        if (roomAdapter == null) {
            roomAdapter = new RoomAdapter(roomList);
            roomsRecyclerView.setAdapter(roomAdapter);
        } else {
            roomAdapter.filterList(roomList);
        }
    }
}
