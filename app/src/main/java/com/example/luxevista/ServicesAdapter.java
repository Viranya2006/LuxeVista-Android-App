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
 * An adapter to manage and display a list of ServiceItem objects in a RecyclerView.
 * It is responsible for creating views for services and binding data to them.
 */
public class ServicesAdapter extends RecyclerView.Adapter<ServicesAdapter.ServiceViewHolder> {

    // The list of services to be displayed.
    private List<ServiceItem> serviceList;

    /**
     * Constructor for the ServicesAdapter.
     * @param serviceList The initial list of services.
     */
    public ServicesAdapter(List<ServiceItem> serviceList) {
        this.serviceList = serviceList;
    }

    /**
     * Updates the adapter's data set with a new filtered list and refreshes the UI.
     * @param filteredList The new list to be displayed.
     */
    public void filterList(List<ServiceItem> filteredList) {
        this.serviceList = filteredList;
        notifyDataSetChanged();
    }

    /**
     * Called by the RecyclerView to create a new ViewHolder.
     * It inflates the layout for a single service card.
     */
    @NonNull
    @Override
    public ServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_in_house_service_card, parent, false);
        return new ServiceViewHolder(view);
    }

    /**
     * Called by the RecyclerView to display the data of a service at a specific position.
     */
    @Override
    public void onBindViewHolder(@NonNull ServiceViewHolder holder, int position) {
        // Get the ServiceItem for the current position.
        ServiceItem currentService = serviceList.get(position);

        // Bind the data to the views.
        holder.serviceImage.setImageResource(currentService.getImageResId());
        holder.serviceName.setText(currentService.getName());
        holder.serviceDescription.setText(currentService.getDescription());
        holder.servicePrice.setText(currentService.getPrice());

        // Set up the click listener for the "Reserve" button.
        holder.bookServiceButton.setOnClickListener(v -> {
            Context context = v.getContext();
            // Create an Intent to navigate to the service booking activity.
            Intent intent = new Intent(context, InHouseServiceBookingActivity.class);
            // Pass the selected ServiceItem object to the next activity.
            intent.putExtra("SERVICE_DETAIL", currentService);
            context.startActivity(intent);
        });
    }

    /**
     * Returns the total number of services in the list.
     */
    @Override
    public int getItemCount() {
        return serviceList.size();
    }

    /**
     * The ViewHolder for a service item, holding references to the UI elements.
     */
    public static class ServiceViewHolder extends RecyclerView.ViewHolder {
        public ImageView serviceImage;
        public TextView serviceName, serviceDescription, servicePrice;
        public Button bookServiceButton;

        public ServiceViewHolder(@NonNull View itemView) {
            super(itemView);
            // Find each view in the item_in_house_service_card.xml layout.
            serviceImage = itemView.findViewById(R.id.service_image);
            serviceName = itemView.findViewById(R.id.service_name);
            serviceDescription = itemView.findViewById(R.id.service_description);
            servicePrice = itemView.findViewById(R.id.service_price);
            bookServiceButton = itemView.findViewById(R.id.book_service_button);
        }
    }
}
