package com.example.luxevista;

import java.io.Serializable;

/**
 * A data model class that represents a single hotel room.
 * It implements Serializable to allow Room objects to be passed between activities via Intents.
 */
public class Room implements Serializable {

    // --- Class properties ---
    private String name;
    private String description;
    private String price;
    private int imageResId;

    /**
     * Constructor for creating a new Room object.
     * @param name The name of the room (e.g., "Ocean View Suite").
     * @param description A brief description of the room.
     * @param price The price per night as a displayable string.
     * @param imageResId The resource ID of the room's image in the drawable folder.
     */
    public Room(String name, String description, String price, int imageResId) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageResId = imageResId;
    }

    // --- Getters to access the properties of the Room object ---

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
