package com.example.luxevista;

import java.io.Serializable;

public class Booking implements Serializable {
    private int id;
    private String roomName;
    private String roomPrice;
    private String checkInDate;  // Changed from bookingDate
    private String checkOutDate; // New field

    // Updated constructor to accept 5 arguments
    public Booking(int id, String roomName, String roomPrice, String checkInDate, String checkOutDate) {
        this.id = id;
        this.roomName = roomName;
        this.roomPrice = roomPrice;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
    }

    // Updated and new Getters
    public int getId() {
        return id;
    }

    public String getRoomName() {
        return roomName;
    }

    public String getRoomPrice() {
        return roomPrice;
    }

    public String getCheckInDate() {
        return checkInDate;
    }

    public String getCheckOutDate() {
        return checkOutDate;
    }
}