package com.example.luxevista;

// A generic class to hold details for any type of booking
public class BookingItem {
    private int id;
    private String title;
    private String detailLine1; // For Room Price or ServiceItem Price
    private String detailLine2; // For Room Dates or ServiceItem Date/Time
    private String bookingType; // To distinguish "room" vs "service"

    public BookingItem(int id, String title, String detailLine1, String detailLine2, String bookingType) {
        this.id = id;
        this.title = title;
        this.detailLine1 = detailLine1;
        this.detailLine2 = detailLine2;
        this.bookingType = bookingType;
    }

    // Getters
    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getDetailLine1() { return detailLine1; }
    public String getDetailLine2() { return detailLine2; }
    public String getBookingType() { return bookingType; }
}