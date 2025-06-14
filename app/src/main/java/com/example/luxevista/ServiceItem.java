package com.example.luxevista;

import java.io.Serializable;

// This class was previously named Service.java
public class ServiceItem implements Serializable {

    private String name;
    private String description;
    private String price;
    private int imageResId; // NEW: Field to hold the image resource ID

    // UPDATED: Constructor now accepts the imageResId
    public ServiceItem(String name, String description, String price, int imageResId) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageResId = imageResId;
    }

    // Getters for all properties
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getPrice() {
        return price;
    }

    // NEW: Getter for the image resource ID
    public int getImageResId() {
        return imageResId;
    }
}