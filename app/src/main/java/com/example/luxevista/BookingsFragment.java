package com.example.luxevista;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

// Note: The class name remains BookingsFragment, but it now handles all bookings
public class BookingsFragment extends Fragment implements BookingAdapter.OnCancelClickListener {

    private RecyclerView bookingsRecyclerView;
    private BookingAdapter bookingAdapter;
    private List<BookingItem> bookingItemList; // Now uses BookingItem
    private DBHelper dbHelper;
    private TextView noBookingsText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bookings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dbHelper = new DBHelper(getContext());
        bookingsRecyclerView = view.findViewById(R.id.bookings_recycler_view);
        noBookingsText = view.findViewById(R.id.text_no_bookings);
        bookingsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void onResume() {
        super.onResume();
        loadBookings();
    }

    private void loadBookings() {
        // Use the new unified method
        bookingItemList = dbHelper.getAllUnifiedBookings();

        if (bookingItemList.isEmpty()) {
            bookingsRecyclerView.setVisibility(View.GONE);
            noBookingsText.setVisibility(View.VISIBLE);
        } else {
            bookingsRecyclerView.setVisibility(View.VISIBLE);
            noBookingsText.setVisibility(View.GONE);
            bookingAdapter = new BookingAdapter(bookingItemList, this);
            bookingsRecyclerView.setAdapter(bookingAdapter);
        }
    }

    // The click listener now needs to check the booking type
    @Override
    public void onCancelClick(int position) {
        // Get the generic item that was clicked
        BookingItem itemToDelete = bookingItemList.get(position);

        // Check the booking type to call the correct delete method
        if ("room".equals(itemToDelete.getBookingType())) {
            dbHelper.deleteBooking(itemToDelete.getId());
            Toast.makeText(getContext(), "Room booking cancelled", Toast.LENGTH_SHORT).show();

        } else if ("service".equals(itemToDelete.getBookingType())) {
            // This is the new, functional logic
            dbHelper.deleteServiceBooking(itemToDelete.getId());
            Toast.makeText(getContext(), "ServiceItem appointment cancelled", Toast.LENGTH_SHORT).show();
        }

        // Refresh the list from the database to show the change immediately
        loadBookings();
    }
}