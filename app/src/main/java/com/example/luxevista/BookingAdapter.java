package com.example.luxevista;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

// This adapter now uses the generic BookingItem
public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.BookingViewHolder> {

    private List<BookingItem> bookingItemList;
    private OnCancelClickListener listener;

    public interface OnCancelClickListener {
        void onCancelClick(int position);
    }

    public BookingAdapter(List<BookingItem> bookingItemList, OnCancelClickListener listener) {
        this.bookingItemList = bookingItemList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // We will reuse the same item_booking_card layout
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_booking_card, parent, false);
        return new BookingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingViewHolder holder, int position) {
        BookingItem currentItem = bookingItemList.get(position);

        holder.roomName.setText(currentItem.getTitle()); // Use the generic title
        holder.roomPrice.setText(currentItem.getDetailLine1()); // Use detailLine1 for price/main detail
        holder.checkInDate.setText(currentItem.getDetailLine2()); // Use detailLine2 for dates/time
        holder.checkOutDate.setVisibility(View.GONE); // Hide the old check-out view
    }

    @Override
    public int getItemCount() {
        return bookingItemList.size();
    }

    public class BookingViewHolder extends RecyclerView.ViewHolder {
        public TextView roomName, roomPrice, checkInDate, checkOutDate;
        public Button cancelButton;

        public BookingViewHolder(@NonNull View itemView) {
            super(itemView);
            roomName = itemView.findViewById(R.id.booking_room_name);
            roomPrice = itemView.findViewById(R.id.booking_room_price);
            checkInDate = itemView.findViewById(R.id.booking_check_in_date);
            checkOutDate = itemView.findViewById(R.id.booking_check_out_date);
            cancelButton = itemView.findViewById(R.id.cancel_booking_button);

            cancelButton.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onCancelClick(position);
                }
            });
        }
    }
}