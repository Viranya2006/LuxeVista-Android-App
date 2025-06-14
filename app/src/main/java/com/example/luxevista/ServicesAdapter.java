package com.example.luxevista;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView; // Import the ImageView class
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ServicesAdapter extends RecyclerView.Adapter<ServicesAdapter.ServiceViewHolder> {

    private List<ServiceItem> serviceList;

    public ServicesAdapter(List<ServiceItem> serviceList) {
        this.serviceList = serviceList;
    }

    public void filterList(List<ServiceItem> filteredList) {
        this.serviceList = filteredList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_in_house_service_card, parent, false);
        return new ServiceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceViewHolder holder, int position) {
        ServiceItem currentService = serviceList.get(position);

        // --- NEW: Set the image for the service ---
        holder.serviceImage.setImageResource(currentService.getImageResId());

        // Set the rest of the data
        holder.serviceName.setText(currentService.getName());
        holder.serviceDescription.setText(currentService.getDescription());
        holder.servicePrice.setText(currentService.getPrice());

        holder.bookServiceButton.setOnClickListener(v -> {
            Context context = v.getContext();
            Intent intent = new Intent(context, InHouseServiceBookingActivity.class);
            intent.putExtra("SERVICE_DETAIL", currentService);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return serviceList.size();
    }

    public static class ServiceViewHolder extends RecyclerView.ViewHolder {
        // --- NEW: Add the ImageView to the ViewHolder ---
        public ImageView serviceImage;
        public TextView serviceName, serviceDescription, servicePrice;
        public Button bookServiceButton;

        public ServiceViewHolder(@NonNull View itemView) {
            super(itemView);
            // --- NEW: Find the ImageView by its ID ---
            serviceImage = itemView.findViewById(R.id.service_image);

            serviceName = itemView.findViewById(R.id.service_name);
            serviceDescription = itemView.findViewById(R.id.service_description);
            servicePrice = itemView.findViewById(R.id.service_price);
            bookServiceButton = itemView.findViewById(R.id.book_service_button);
        }
    }
}
