package com.example.luxevista;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * An adapter to manage and display a list of Room objects in a RecyclerView.
 * It handles the creation of view holders and binds the room data to the UI.
 */
public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.RoomViewHolder> {

    // The list of rooms to be displayed.
    private List<Room> roomList;

    /**
     * Constructor for the RoomAdapter.
     * @param roomList The initial list of rooms to display.
     */
    public RoomAdapter(List<Room> roomList) {
        this.roomList = roomList;
    }

    /**
     * A method to update the list of rooms with new data and refresh the RecyclerView.
     * This is used by the search and filter functionality.
     * @param filteredList The new, filtered list of rooms to display.
     */
    public void filterList(List<Room> filteredList) {
        this.roomList = filteredList;
        // notifyDataSetChanged tells the RecyclerView that the data has changed and it needs to redraw itself.
        notifyDataSetChanged();
    }

    /**
     * Called by the RecyclerView when it needs to create a new ViewHolder for an item.
     * @param parent The ViewGroup into which the new View will be added.
     * @param viewType The view type of the new View.
     * @return A new RoomViewHolder that holds the View for each list item.
     */
    @NonNull
    @Override
    public RoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the custom layout for a single room card.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_room_card, parent, false);
        return new RoomViewHolder(view);
    }

    /**
     * Called by the RecyclerView to display the data at a specific position.
     * This method updates the contents of the ViewHolder to reflect the room at the given position.
     * @param holder The ViewHolder which should be updated.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull RoomViewHolder holder, int position) {
        // Get the Room object for the current position.
        Room currentRoom = roomList.get(position);

        // Bind the data from the Room object to the views in the ViewHolder.
        holder.roomImage.setImageResource(currentRoom.getImageResId());
        holder.roomName.setText(currentRoom.getName());
        holder.roomDescription.setText(currentRoom.getDescription());
        holder.roomPrice.setText(currentRoom.getPrice());

        // Set up the click listener for the "Details" button.
        holder.detailsButton.setOnClickListener(v -> {
            Context context = v.getContext();
            // Create an Intent to navigate to the RoomDetailActivity.
            Intent intent = new Intent(context, RoomDetailActivity.class);
            // Pass the entire Room object to the detail activity.
            intent.putExtra("ROOM_DETAIL", currentRoom);
            context.startActivity(intent);
        });
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     * @return The total number of rooms in the list.
     */
    @Override
    public int getItemCount() {
        return roomList.size();
    }

    /**
     * The ViewHolder class. It holds references to the UI views for a single list item.
     * This is a performance optimization to avoid repeated calls to findViewById().
     */
    public static class RoomViewHolder extends RecyclerView.ViewHolder {
        public ImageView roomImage;
        public TextView roomName, roomDescription, roomPrice;
        public Button detailsButton;

        public RoomViewHolder(@NonNull View itemView) {
            super(itemView);
            // Find each view in the item_room_card.xml layout.
            roomImage = itemView.findViewById(R.id.room_image);
            roomName = itemView.findViewById(R.id.room_name);
            roomDescription = itemView.findViewById(R.id.room_description);
            roomPrice = itemView.findViewById(R.id.room_price);
            detailsButton = itemView.findViewById(R.id.details_button);
        }
    }
}
