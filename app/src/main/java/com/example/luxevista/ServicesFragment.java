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

public class ServicesFragment extends Fragment {

    private RecyclerView servicesRecyclerView;

    // --- FIX #1: The variable type was incorrect. It must be ServicesAdapter. ---
    private ServicesAdapter servicesAdapter;

    private List<ServiceItem> serviceList;
    private DBHelper dbHelper;
    private SearchView searchView;
    private Spinner filterSpinner, sortSpinner;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_services, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dbHelper = new DBHelper(getContext());
        servicesRecyclerView = view.findViewById(R.id.services_recycler_view);
        servicesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        searchView = view.findViewById(R.id.services_search_view);
        filterSpinner = view.findViewById(R.id.services_filter_spinner);
        sortSpinner = view.findViewById(R.id.services_sort_spinner);

        setupSpinners();
        setupListeners();
        updateServiceList();
    }

    private void setupSpinners() {
        if (getContext() == null) return;
        ArrayAdapter<CharSequence> filterAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.service_category_array, android.R.layout.simple_spinner_item);
        filterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        filterSpinner.setAdapter(filterAdapter);

        ArrayAdapter<CharSequence> sortAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.service_sort_array, android.R.layout.simple_spinner_item);
        sortAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortSpinner.setAdapter(sortAdapter);
    }

    private void setupListeners() {
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

    private void updateServiceList() {
        if (getContext() == null) return;

        String searchQuery = searchView.getQuery().toString();
        String categoryFilter = filterSpinner.getSelectedItem().toString();
        String sortOrder = sortSpinner.getSelectedItem().toString();

        serviceList = dbHelper.getFilteredAndSortedServices(categoryFilter, searchQuery, sortOrder);

        if (servicesAdapter == null) {
            // --- FIX #2: You must create an instance of ServicesAdapter, not ServicesFragment. ---
            servicesAdapter = new ServicesAdapter(serviceList);
            servicesRecyclerView.setAdapter(servicesAdapter);
        } else {
            // --- FIX #3: This line now works correctly because servicesAdapter is the correct type. ---
            servicesAdapter.filterList(serviceList);
        }
    }
}
