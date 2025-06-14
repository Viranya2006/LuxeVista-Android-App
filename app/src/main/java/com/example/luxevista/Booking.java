package com.example.luxevista;

import java.io.Serializable;

/**
 * A data model class specifically representing a single room booking record.
 */
public class Booking implements Serializable {
    private int id;
    private String roomName;
    private String roomPrice;
    private String checkInDate;
    private String checkOutDate;

    /**
     * Constructor for creating a new Booking object.
     * @param id The unique ID of the booking from the database.
     * @param roomName The name of the booked room.
     * @param roomPrice The price of the booked room.
     * @param checkInDate The check-in date for the booking.
     * @param checkOutDate The check-out date for the booking.
     */
    public Booking(int id, String roomName, String roomPrice, String checkInDate, String checkOutDate) {
        this.id = id;
        this.roomName = roomName;
        this.roomPrice = roomPrice;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
    }

    // --- Getters for accessing booking properties ---
    public int getId() { return id; }
    public String getRoomName() { return roomName; }
    public String getRoomPrice() { return roomPrice; }
    public String getCheckInDate() { return checkInDate; }
    public String getCheckOutDate() { return checkOutDate; }
}