package com.example.luxevista;

import java.io.Serializable;

/**
 * A data model class representing a single in-house service (e.g., spa, dining).
 * It implements Serializable so it can be passed to the booking activity.
 */
public class ServiceItem implements Serializable {

    // --- Class properties ---
    private String name;
    private String description;
    private String price;
    private int imageResId;

    /**
     * Constructor for creating a new ServiceItem object.
     * @param name The name of the service.
     * @param description A brief description of the service.
     * @param price The price of the service as a displayable string.
     * @param imageResId The resource ID of the service's image.
     */
    public ServiceItem(String name, String description, String price, int imageResId) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageResId = imageResId;
    }

    // --- Getters for accessing the properties ---

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getPrice() {
        return price;
    }

    public int getImageResId() {
        return imageResId;
    }
}
