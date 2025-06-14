package com.example.luxevista;

import java.io.Serializable; // Import this

// Add "implements Serializable"
public class Room implements Serializable {
    private String name;
    private String description;
    private String price;
    private int imageResId;

    // The constructor remains the same
    public Room(String name, String description, String price, int imageResId) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageResId = imageResId;
    }

    // Getters remain the same
    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getPrice() { return price; }
    public int getImageResId() { return imageResId; }
}