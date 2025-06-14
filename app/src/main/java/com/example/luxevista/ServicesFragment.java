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
 * The ServicesFragment displays a complete list of all in-house services offered by the resort.
 * It provides UI controls for searching, filtering by service category, and sorting,
 * allowing guests to explore all available amenities.
 */
public class ServicesFragment extends Fragment {

    // UI Components
    private RecyclerView servicesRecyclerView;
    private SearchView searchView;
    private Spinner filterSpinner, sortSpinner;

    // Data and Helpers
    private ServicesAdapter servicesAdapter;
    private List<ServiceItem> serviceList;
    private DBHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the XML layout for this fragment.
        return inflater.inflate(R.layout.fragment_services, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize the database helper and UI components.
        dbHelper = new DBHelper(getContext());
        servicesRecyclerView = view.findViewById(R.id.services_recycler_view);
        servicesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        searchView = view.findViewById(R.id.services_search_view);
        filterSpinner = view.findViewById(R.id.services_filter_spinner);
        sortSpinner = view.findViewById(R.id.services_sort_spinner);

        // Populate the dropdown menus and set up their listeners.
        setupSpinners();
        setupListeners();

        // Perform an initial load of the service list.
        updateServiceList();
    }

    /**
     * Populates the filter and sort Spinners with data from the arrays.xml resource file.
     */
    private void setupSpinners() {
        if (getContext() == null) return;
        // Set up the adapter for the service category filter.
        ArrayAdapter<CharSequence> filterAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.service_category_array, android.R.layout.simple_spinner_item);
        filterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        filterSpinner.setAdapter(filterAdapter);

        // Set up the adapter for the service sort options.
        ArrayAdapter<CharSequence> sortAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.service_sort_array, android.R.layout.simple_spinner_item);
        sortAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortSpinner.setAdapter(sortAdapter);
    }

    /**
     * Attaches listeners to the search, filter, and sort controls to react to user input.
     */
    private void setupListeners() {
        // Listener for the search bar.
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                updateServiceList();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                updateServiceList();
                return true;
            }
        });

        // A shared listener for both dropdown menus.
        AdapterView.OnItemSelectedListener listener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateServiceList();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        };
        filterSpinner.setOnItemSelectedListener(listener);
        sortSpinner.setOnItemSelectedListener(listener);
    }

    /**
     * This is the master update method for the services list. It fetches the current state
     * of the UI controls and queries the database to update the RecyclerView.
     */
    private void updateServiceList() {
        if (getContext() == null) return;

        // Get current values from the search bar and spinners.
        String searchQuery = searchView.getQuery().toString();
        String categoryFilter = filterSpinner.getSelectedItem().toString();
        String sortOrder = sortSpinner.getSelectedItem().toString();

        // Fetch the filtered and sorted list of services from the database.
        serviceList = dbHelper.getFilteredAndSortedServices(categoryFilter, searchQuery, sortOrder);

        // Update the adapter with the new list.
        if (servicesAdapter == null) {
            servicesAdapter = new ServicesAdapter(serviceList);
            servicesRecyclerView.setAdapter(servicesAdapter);
        } else {
            servicesAdapter.filterList(serviceList);
        }
    }
}
