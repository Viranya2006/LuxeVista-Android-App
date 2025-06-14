package com.example.luxevista;

/**
 * A generic data model class used to display items in the unified "My Bookings" list.
 * It can hold data for either a room booking or a service booking.
 */
public class BookingItem {
    private int id;
    private String title;       // Can be room name or service name
    private String detailLine1; // Can be room price or service price/details
    private String detailLine2; // Can be room dates or service appointment date/time
    private String bookingType; // A flag ("room" or "service") to distinguish the booking type

    /**
     * Constructor for creating a generic BookingItem.
     * @param id The unique ID from its original database table.
     * @param title The main title of the booking.
     * @param detailLine1 The first line of detail text.
     * @param detailLine2 The second line of detail text.
     * @param bookingType A string indicating the type ("room" or "service").
     */
    public BookingItem(int id, String title, String detailLine1, String detailLine2, String bookingType) {
        this.id = id;
        this.title = title;
        this.detailLine1 = detailLine1;
        this.detailLine2 = detailLine2;
        this.bookingType = bookingType;
    }

    // --- Getters for accessing generic booking properties ---
    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getDetailLine1() { return detailLine1; }
    public String getDetailLine2() { return detailLine2; }
    public String getBookingType() { return bookingType; }
}
