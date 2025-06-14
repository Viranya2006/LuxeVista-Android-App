package com.example.luxevista;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

/**
 * An adapter to display a unified list of both room and service bookings.
 * It uses the generic BookingItem model to handle different types of bookings.
 */
public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.BookingViewHolder> {

    // A list of generic BookingItem objects.
    private List<BookingItem> bookingItemList;
    // A listener to handle click events back in the fragment.
    private OnCancelClickListener listener;

    /**
     * An interface to define the click listener that the fragment will implement.
     * This is a clean way to handle clicks without bloating the adapter with business logic.
     */
    public interface OnCancelClickListener {
        void onCancelClick(int position);
    }

    /**
     * Constructor for the BookingAdapter.
     * @param bookingItemList The list of unified booking items.
     * @param listener The fragment that will listen for cancel clicks.
     */
    public BookingAdapter(List<BookingItem> bookingItemList, OnCancelClickListener listener) {
        this.bookingItemList = bookingItemList;
        this.listener = listener;
    }

    /**
     * Called to create a new ViewHolder for a booking item.
     */
    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // We reuse the same card layout for all booking types.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_booking_card, parent, false);
        return new BookingViewHolder(view);
    }

    /**
     * Binds the data from a generic BookingItem object to the views.
     */
    @Override
    public void onBindViewHolder(@NonNull BookingViewHolder holder, int position) {
        BookingItem currentItem = bookingItemList.get(position);

        // Populate the views with data from the BookingItem.
        holder.title.setText(currentItem.getTitle());
        holder.detailLine1.setText(currentItem.getDetailLine1());
        holder.detailLine2.setText(currentItem.getDetailLine2());

        // For service bookings, the price line might be empty, so we hide it.
        if (currentItem.getDetailLine1() == null || currentItem.getDetailLine1().isEmpty()) {
            holder.detailLine1.setVisibility(View.GONE);
        } else {
            holder.detailLine1.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Returns the total number of items in the booking list.
     */
    @Override
    public int getItemCount() {
        return bookingItemList.size();
    }

    /**
     * The ViewHolder for a booking item.
     * It uses more generic names to accommodate different booking types.
     */
    public class BookingViewHolder extends RecyclerView.ViewHolder {
        public TextView title, detailLine1, detailLine2;
        public Button cancelButton;

        public BookingViewHolder(@NonNull View itemView) {
            super(itemView);
            // Link to the views in item_booking_card.xml.
            title = itemView.findViewById(R.id.booking_room_name);
            detailLine1 = itemView.findViewById(R.id.booking_room_price);
            detailLine2 = itemView.findViewById(R.id.booking_check_in_date);
            // Hide the second date text view as it's not needed for the unified display.
            itemView.findViewById(R.id.booking_check_out_date).setVisibility(View.GONE);

            cancelButton = itemView.findViewById(R.id.cancel_booking_button);

            // Set up the click listener for the cancel button.
            cancelButton.setOnClickListener(v -> {
                int position = getAdapterPosition();
                // Ensure the listener exists and the position is valid.
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    // Trigger the onCancelClick method implemented in the fragment.
                    listener.onCancelClick(position);
                }
            });
        }
    }
}
