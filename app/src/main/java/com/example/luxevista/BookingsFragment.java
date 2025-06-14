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

/**
 * Manages the "My Bookings" screen, which displays a unified list of the user's
 * room bookings and service reservations. It implements the OnCancelClickListener
 * interface to handle booking cancellation requests from its adapter.
 */
public class BookingsFragment extends Fragment implements BookingAdapter.OnCancelClickListener {

    // UI Components
    private RecyclerView bookingsRecyclerView;
    private TextView noBookingsText;

    // Data and Helpers
    private BookingAdapter bookingAdapter;
    private List<BookingItem> bookingItemList;
    private DBHelper dbHelper;

    /**
     * Called to create the view for this fragment.
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout defined in fragment_bookings.xml.
        return inflater.inflate(R.layout.fragment_bookings, container, false);
    }

    /**
     * Called after the view has been created. This is where we initialize our components.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Initialize the database helper.
        dbHelper = new DBHelper(getContext());
        // Link UI components to their views in the layout.
        bookingsRecyclerView = view.findViewById(R.id.bookings_recycler_view);
        noBookingsText = view.findViewById(R.id.text_no_bookings);
        // Set up the RecyclerView to display items in a vertical list.
        bookingsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    /**
     * Called every time the fragment becomes visible to the user.
     * This is the ideal place to refresh the list to ensure it's always up-to-date.
     */
    @Override
    public void onResume() {
        super.onResume();
        loadBookings();
    }

    /**
     * Fetches the unified list of all bookings from the database and updates the RecyclerView.
     */
    private void loadBookings() {
        // Get the combined list of room and service bookings from the database.
        bookingItemList = dbHelper.getAllUnifiedBookings();

        // Check if the list is empty.
        if (bookingItemList.isEmpty()) {
            // If there are no bookings, show the "empty" message and hide the list.
            bookingsRecyclerView.setVisibility(View.GONE);
            noBookingsText.setVisibility(View.VISIBLE);
        } else {
            // If there are bookings, show the list and hide the "empty" message.
            bookingsRecyclerView.setVisibility(View.VISIBLE);
            noBookingsText.setVisibility(View.GONE);
            // Create the adapter, passing the data list and 'this' fragment as the click listener.
            bookingAdapter = new BookingAdapter(bookingItemList, this);
            bookingsRecyclerView.setAdapter(bookingAdapter);
        }
    }

    /**
     * This method is the implementation of the OnCancelClickListener interface from the adapter.
     * It is called whenever the "Cancel" button on a booking card is clicked.
     * @param position The position of the item in the list that was clicked.
     */
    @Override
    public void onCancelClick(int position) {
        // Get the specific BookingItem that needs to be deleted.
        BookingItem itemToDelete = bookingItemList.get(position);

        // Check the type of the booking to call the correct delete method in the DBHelper.
        if ("room".equals(itemToDelete.getBookingType())) {
            dbHelper.deleteBooking(itemToDelete.getId());
            Toast.makeText(getContext(), "Room booking cancelled", Toast.LENGTH_SHORT).show();
        } else if ("service".equals(itemToDelete.getBookingType())) {
            dbHelper.deleteServiceBooking(itemToDelete.getId());
            Toast.makeText(getContext(), "Service appointment cancelled", Toast.LENGTH_SHORT).show();
        }

        // After deleting from the database, refresh the list on the screen to show the change.
        loadBookings();
    }
}
