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

import java.util.ArrayList;
import java.util.List;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.RoomViewHolder> {

    // This list will be updated with filtered results
    private List<Room> roomList;

    public RoomAdapter(List<Room> roomList) {
        this.roomList = roomList;
    }

    /**
     * This is the new method that fixes the error.
     * It updates the adapter's list with a new filtered list and refreshes the display.
     * @param filteredList The new list to be displayed.
     */
    public void filterList(List<Room> filteredList) {
        this.roomList = filteredList;
        notifyDataSetChanged(); // This tells the RecyclerView to redraw itself
    }

    @NonNull
    @Override
    public RoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_room_card, parent, false);
        return new RoomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomViewHolder holder, int position) {
        Room currentRoom = roomList.get(position);

        holder.roomImage.setImageResource(currentRoom.getImageResId());
        holder.roomName.setText(currentRoom.getName());
        holder.roomDescription.setText(currentRoom.getDescription());
        holder.roomPrice.setText(currentRoom.getPrice());

        holder.detailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, RoomDetailActivity.class);
                intent.putExtra("ROOM_DETAIL", currentRoom);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return roomList.size();
    }

    public static class RoomViewHolder extends RecyclerView.ViewHolder {
        public ImageView roomImage;
        public TextView roomName;
        public TextView roomDescription;
        public TextView roomPrice;
        public Button detailsButton;

        public RoomViewHolder(@NonNull View itemView) {
            super(itemView);
            roomImage = itemView.findViewById(R.id.room_image);
            roomName = itemView.findViewById(R.id.room_name);
            roomDescription = itemView.findViewById(R.id.room_description);
            roomPrice = itemView.findViewById(R.id.room_price);
            detailsButton = itemView.findViewById(R.id.details_button);
        }
    }
}
