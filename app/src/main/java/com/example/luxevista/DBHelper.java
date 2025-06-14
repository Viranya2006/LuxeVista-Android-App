package com.example.luxevista;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages all database creation and version management for the LuxeVista app.
 * This class serves as the single point of interaction with the SQLite database,
 * handling all CRUD (Create, Read, Update, Delete) operations.
 */
public class DBHelper extends SQLiteOpenHelper {

    // The version number of the database. Incrementing this will trigger the onUpgrade method.
    private static final int DATABASE_VERSION = 9;
    // The name of the database file that will be stored on the device.
    private static final String DATABASE_NAME = "LuxeVista.db";

    // --- Table and Column Constants for the 'users' table ---
    public static final String TABLE_USERS = "users";
    public static final String COLUMN_USER_ID = "id";
    public static final String COLUMN_USER_NAME = "name";
    public static final String COLUMN_USER_EMAIL = "email";
    public static final String COLUMN_USER_PASSWORD = "password";
    public static final String COLUMN_USER_CONTACT = "contact_number";

    // --- Table and Column Constants for the 'rooms' table ---
    public static final String TABLE_ROOMS = "rooms";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_PRICE = "price";
    public static final String COLUMN_IMAGE_RES_ID = "imageResId";
    public static final String COLUMN_ROOM_TYPE = "room_type";
    public static final String COLUMN_PRICE_NUMERIC = "price_numeric";

    // --- Table and Column Constants for the 'bookings' table (for rooms) ---
    public static final String TABLE_BOOKINGS = "bookings";
    public static final String COLUMN_BOOKING_ID = "booking_id";
    public static final String COLUMN_BOOKED_ROOM_NAME = "room_name";
    public static final String COLUMN_BOOKED_ROOM_PRICE = "room_price";
    public static final String COLUMN_CHECK_IN_DATE = "check_in_date";
    public static final String COLUMN_CHECK_OUT_DATE = "check_out_date";

    // --- Table and Column Constants for the 'services' table ---
    public static final String TABLE_SERVICES = "services";
    public static final String COLUMN_SERVICE_ID = "service_id";
    public static final String COLUMN_SERVICE_NAME = "service_name";
    public static final String COLUMN_SERVICE_DESC = "service_description";
    public static final String COLUMN_SERVICE_PRICE = "service_price";
    public static final String COLUMN_SERVICE_CATEGORY = "category";
    public static final String COLUMN_SERVICE_IMAGE_RES_ID = "imageResId";

    // --- Table and Column Constants for the 'service_bookings' table ---
    public static final String TABLE_SERVICE_BOOKINGS = "service_bookings";
    public static final String COLUMN_SERVICE_BOOKING_ID = "service_booking_id";
    public static final String COLUMN_BOOKED_SERVICE_NAME = "service_name";
    public static final String COLUMN_APPOINTMENT_DATE = "appointment_date";
    public static final String COLUMN_APPOINTMENT_TIME = "appointment_time";

    // --- SQL CREATE Statements for all tables ---
    private static final String TABLE_USERS_CREATE = "CREATE TABLE " + TABLE_USERS + " (" + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_USER_NAME + " TEXT, " + COLUMN_USER_EMAIL + " TEXT UNIQUE, " + COLUMN_USER_PASSWORD + " TEXT, " + COLUMN_USER_CONTACT + " TEXT);";
    private static final String TABLE_ROOMS_CREATE = "CREATE TABLE " + TABLE_ROOMS + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_NAME + " TEXT, " + COLUMN_DESCRIPTION + " TEXT, " + COLUMN_PRICE + " TEXT, " + COLUMN_IMAGE_RES_ID + " INTEGER, " + COLUMN_ROOM_TYPE + " TEXT, " + COLUMN_PRICE_NUMERIC + " REAL);";
    private static final String TABLE_BOOKINGS_CREATE = "CREATE TABLE " + TABLE_BOOKINGS + " (" + COLUMN_BOOKING_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_BOOKED_ROOM_NAME + " TEXT, " + COLUMN_BOOKED_ROOM_PRICE + " TEXT, " + COLUMN_CHECK_IN_DATE + " TEXT, " + COLUMN_CHECK_OUT_DATE + " TEXT);";
    private static final String TABLE_SERVICES_CREATE = "CREATE TABLE " + TABLE_SERVICES + " (" + COLUMN_SERVICE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_SERVICE_NAME + " TEXT, " + COLUMN_SERVICE_DESC + " TEXT, " + COLUMN_SERVICE_PRICE + " TEXT, " + COLUMN_SERVICE_CATEGORY + " TEXT, " + COLUMN_SERVICE_IMAGE_RES_ID + " INTEGER);";
    private static final String TABLE_SERVICE_BOOKINGS_CREATE = "CREATE TABLE " + TABLE_SERVICE_BOOKINGS + " (" + COLUMN_SERVICE_BOOKING_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_BOOKED_SERVICE_NAME + " TEXT, " + COLUMN_APPOINTMENT_DATE + " TEXT, " + COLUMN_APPOINTMENT_TIME + " TEXT);";

    /**
     * Constructor for the DBHelper.
     * @param context The application context.
     */
    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Called when the database is first created. This is where we define the
     * table structure and insert any initial data needed for the app to function.
     * @param db The database.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_USERS_CREATE);
        db.execSQL(TABLE_ROOMS_CREATE);
        db.execSQL(TABLE_BOOKINGS_CREATE);
        db.execSQL(TABLE_SERVICES_CREATE);
        db.execSQL(TABLE_SERVICE_BOOKINGS_CREATE);
        insertInitialRooms(db);
        insertInitialServices(db);
    }

    /**
     * Called when the database needs to be upgraded, for example, when the DATABASE_VERSION is incremented.
     * This method handles migrating the database to a new schema.
     * @param db The database.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // A simple upgrade policy: drop all tables and recreate the database.
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ROOMS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOKINGS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SERVICES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SERVICE_BOOKINGS);
        onCreate(db);
    }

    //USER METHODS

    /**
     * Adds a new user to the users table. Used during registration.
     * @param name The user's full name.
     * @param email The user's email address.
     * @param password The user's chosen password.
     * @param contact The user's contact number.
     * @return true if the user was added successfully, false otherwise.
     */
    public boolean addUser(String name, String email, String password, String contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, name);
        values.put(COLUMN_USER_EMAIL, email);
        values.put(COLUMN_USER_PASSWORD, password);
        values.put(COLUMN_USER_CONTACT, contact);
        long result = db.insert(TABLE_USERS, null, values);
        db.close();
        return result != -1;
    }

    /**
     * Checks if a user exists with the provided email and password. Used for login validation.
     * @param email The user's email to check.
     * @param password The user's password to check.
     * @return true if credentials match a user in the database, false otherwise.
     */
    public boolean checkUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{COLUMN_USER_ID}, COLUMN_USER_EMAIL + " = ? AND " + COLUMN_USER_PASSWORD + " = ?", new String[]{email, password}, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        return count > 0;
    }

    /**
     * Retrieves a user's name based on their email address. Used for personalizing the UI.
     * @param email The email of the user whose name is to be fetched.
     * @return The user's name as a String, or null if not found.
     */
    public String getUserName(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{COLUMN_USER_NAME}, COLUMN_USER_EMAIL + "=?", new String[]{email}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            int nameIndex = cursor.getColumnIndex(COLUMN_USER_NAME);
            if (nameIndex != -1) {
                String name = cursor.getString(nameIndex);
                cursor.close();
                return name;
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        return null;
    }

    /**
     * Updates the details of an existing user in the database.
     * @param originalEmail The current email of the user to identify the record.
     * @param newName The new full name for the user.
     * @param newContact The new contact number for the user.
     * @param newEmail The new email address for the user.
     * @param newPassword The new password for the user (can be empty if not changing).
     * @return true if the update was successful, false otherwise.
     */
    public boolean updateUser(String originalEmail, String newName, String newContact, String newEmail, String newPassword) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, newName);
        values.put(COLUMN_USER_CONTACT, newContact);
        values.put(COLUMN_USER_EMAIL, newEmail);
        if (newPassword != null && !newPassword.isEmpty()) {
            values.put(COLUMN_USER_PASSWORD, newPassword);
        }
        int rowsAffected = db.update(TABLE_USERS, values, COLUMN_USER_EMAIL + " = ?", new String[]{originalEmail});
        db.close();
        return rowsAffected > 0;
    }

    /**
     * Retrieves all details for a specific user based on their email.
     * @param email The email of the user to fetch.
     * @return A Cursor object containing the user's data.
     */
    public Cursor getUserDetails(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_USERS, null, COLUMN_USER_EMAIL + "=?", new String[]{email}, null, null, null);
    }

    //ROOM METHODS

    /**
     * Private helper method to insert the initial set of hotel rooms into the database.
     */
    private void insertInitialRooms(SQLiteDatabase db) {
        addRoom(db, "Ocean View Suite", "Stunning panoramic views of the ocean.", "Rs. 32,500 / night", R.drawable.room_image_1, "Suite", 57500);
        addRoom(db, "Deluxe Room", "Spacious comfort with premium amenities.", "Rs. 25,00 / night", R.drawable.room_image_2, "Deluxe", 46500);
        addRoom(db, "Executive Pool Villa", "Ultimate privacy with your own personal pool.", "Rs. 38,000 / night", R.drawable.room_image_3, "Villa", 73200);
        addRoom(db, "Family Suite", "Two-bedroom suite perfect for the whole family.", "Rs. 26,000 / night", R.drawable.room_image_4, "Suite", 39000);
        addRoom(db, "Honeymoon Penthouse", "The pinnacle of luxury with a private terrace.", "Rs. 39,000 / night", R.drawable.room_image_5, "Penthouse", 96000);
    }

    /**
     * Private helper to streamline adding a new room to the database.
     */
    private void addRoom(SQLiteDatabase db, String name, String desc, String price, int imgId, String type, double numericPrice) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_DESCRIPTION, desc);
        values.put(COLUMN_PRICE, price);
        values.put(COLUMN_IMAGE_RES_ID, imgId);
        values.put(COLUMN_ROOM_TYPE, type);
        values.put(COLUMN_PRICE_NUMERIC, numericPrice);
        db.insert(TABLE_ROOMS, null, values);
    }

    /**
     * Retrieves a list of all rooms. A convenience method for the Home screen.
     * @return A list of all Room objects.
     */
    public List<Room> getAllRooms() {
        return getFilteredAndSortedRooms("All Types", "", "Default");
    }

    /**
     * Retrieves a list of rooms based on filter, search, and sort criteria.
     * @param roomTypeFilter The category to filter by (e.g., "Suite").
     * @param searchQuery The text to search for in the room name.
     * @param sortOrder The order to sort the results by (e.g., "Price: Low to High").
     * @return A filtered and sorted list of Room objects.
     */
    public List<Room> getFilteredAndSortedRooms(String roomTypeFilter, String searchQuery, String sortOrder) {
        List<Room> roomList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = "1=1";
        List<String> selectionArgs = new ArrayList<>();
        if (roomTypeFilter != null && !roomTypeFilter.equalsIgnoreCase("All Types")) {
            selection += " AND " + COLUMN_ROOM_TYPE + " = ?";
            selectionArgs.add(roomTypeFilter);
        }
        if (searchQuery != null && !searchQuery.isEmpty()) {
            selection += " AND " + COLUMN_NAME + " LIKE ?";
            selectionArgs.add("%" + searchQuery + "%");
        }
        String orderBy = null;
        if (sortOrder != null) {
            if (sortOrder.equalsIgnoreCase("Price: Low to High")) {
                orderBy = COLUMN_PRICE_NUMERIC + " ASC";
            } else if (sortOrder.equalsIgnoreCase("Price: High to Low")) {
                orderBy = COLUMN_PRICE_NUMERIC + " DESC";
            }
        }
        Cursor cursor = db.query(TABLE_ROOMS, null, selection, selectionArgs.toArray(new String[0]), null, null, orderBy);
        if (cursor.moveToFirst()) {
            do {
                int nameIndex = cursor.getColumnIndex(COLUMN_NAME);
                int descriptionIndex = cursor.getColumnIndex(COLUMN_DESCRIPTION);
                int priceIndex = cursor.getColumnIndex(COLUMN_PRICE);
                int imageResIdIndex = cursor.getColumnIndex(COLUMN_IMAGE_RES_ID);
                if (nameIndex != -1 && descriptionIndex != -1 && priceIndex != -1 && imageResIdIndex != -1) {
                    Room room = new Room(cursor.getString(nameIndex), cursor.getString(descriptionIndex), cursor.getString(priceIndex), cursor.getInt(imageResIdIndex));
                    roomList.add(room);
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        return roomList;
    }

    //BOOKING METHODS

    /**
     * Adds a new room booking to the database.
     * @param room The Room object being booked.
     * @param checkInDate The selected check-in date.
     * @param checkOutDate The selected check-out date.
     */
    public void addBooking(Room room, String checkInDate, String checkOutDate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_BOOKED_ROOM_NAME, room.getName());
        values.put(COLUMN_BOOKED_ROOM_PRICE, room.getPrice());
        values.put(COLUMN_CHECK_IN_DATE, checkInDate);
        values.put(COLUMN_CHECK_OUT_DATE, checkOutDate);
        db.insert(TABLE_BOOKINGS, null, values);
        db.close();
    }

    /**
     * Retrieves a combined list of all room and service bookings for the "My Bookings" screen.
     * @return A list of generic BookingItem objects.
     */
    public List<BookingItem> getAllUnifiedBookings() {
        List<BookingItem> unifiedList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor roomCursor = db.rawQuery("SELECT * FROM " + TABLE_BOOKINGS, null);
        if (roomCursor.moveToFirst()) {
            do {
                int idIndex = roomCursor.getColumnIndex(COLUMN_BOOKING_ID);
                int nameIndex = roomCursor.getColumnIndex(COLUMN_BOOKED_ROOM_NAME);
                int priceIndex = roomCursor.getColumnIndex(COLUMN_BOOKED_ROOM_PRICE);
                int checkInIndex = roomCursor.getColumnIndex(COLUMN_CHECK_IN_DATE);
                int checkOutIndex = roomCursor.getColumnIndex(COLUMN_CHECK_OUT_DATE);
                if (idIndex != -1 && nameIndex != -1 && priceIndex != -1 && checkInIndex != -1 && checkOutIndex != -1) {
                    int id = roomCursor.getInt(idIndex);
                    String title = roomCursor.getString(nameIndex);
                    String price = roomCursor.getString(priceIndex);
                    String dates = "Check-in: " + roomCursor.getString(checkInIndex) + "\nCheck-out: " + roomCursor.getString(checkOutIndex);
                    unifiedList.add(new BookingItem(id, title, price, dates, "room"));
                }
            } while (roomCursor.moveToNext());
        }
        roomCursor.close();
        Cursor serviceCursor = db.rawQuery("SELECT * FROM " + TABLE_SERVICE_BOOKINGS, null);
        if (serviceCursor.moveToFirst()) {
            do {
                int idIndex = serviceCursor.getColumnIndex(COLUMN_SERVICE_BOOKING_ID);
                int nameIndex = serviceCursor.getColumnIndex(COLUMN_BOOKED_SERVICE_NAME);
                int dateIndex = serviceCursor.getColumnIndex(COLUMN_APPOINTMENT_DATE);
                int timeIndex = serviceCursor.getColumnIndex(COLUMN_APPOINTMENT_TIME);
                if (idIndex != -1 && nameIndex != -1 && dateIndex != -1 && timeIndex != -1) {
                    int id = serviceCursor.getInt(idIndex);
                    String title = serviceCursor.getString(nameIndex);
                    String dateTime = "Appointment: " + serviceCursor.getString(dateIndex) + " at " + serviceCursor.getString(timeIndex);
                    unifiedList.add(new BookingItem(id, title, "", dateTime, "service"));
                }
            } while (serviceCursor.moveToNext());
        }
        serviceCursor.close();
        return unifiedList;
    }

    /**
     * Deletes a room booking from the database based on its ID.
     * @param bookingId The ID of the room booking to delete.
     */
    public void deleteBooking(int bookingId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_BOOKINGS, COLUMN_BOOKING_ID + " = ?", new String[]{String.valueOf(bookingId)});
        db.close();
    }

    //SERVICE METHODS

    /**
     * Private helper method to insert the initial set of in-house services into the database.
     */
    private void insertInitialServices(SQLiteDatabase db) {
        addService(db, "Swedish Massage", "A relaxing full-body massage for ultimate rejuvenation.", "Rs. 18,000 / 60 min", "Spa Treatment", R.drawable.swedish_massage);
        addService(db, "Hot Stone Therapy", "Eases muscle tension and pain using heated stones.", "Rs. 22,000 / 75 min", "Spa Treatment", R.drawable.hot_stone_therapy);
        addService(db, "Aromatherapy Facial", "A custom facial using essential oils to nourish skin.", "Rs. 15,000 / 50 min", "Spa Treatment", R.drawable.aromatherapy_facial);
        addService(db, "Deep Tissue Massage", "Focuses on the deepest layers of muscle tissue, tendons and fascia.", "Rs. 20,000 / 60 min", "Spa Treatment", R.drawable.deep_tissue_massage);
        addService(db, "Couples' Retreat", "A shared massage experience for two in our private suite.", "Rs. 35,000 / 60 min", "Spa Treatment", R.drawable.couples_retreat);
        addService(db, "Seafood Platter", "Freshly caught lobster, prawns, and crab.", "Rs. 28,000", "Fine Dining", R.drawable.seafood_platter);
        addService(db, "Wagyu Steak", "8oz premium Wagyu beef served with truffle mash.", "Rs. 38,000", "Fine Dining", R.drawable.wagyu_steak);
        addService(db, "Chef's Tasting Menu", "A five-course journey through modern Sri Lankan cuisine.", "Rs. 45,000 per person", "Fine Dining", R.drawable.tasting_menu);
        addService(db, "Private Beach Dinner", "A romantic dinner for two under the stars on a private beach.", "Rs. 90,000", "Fine Dining", R.drawable.private_beach_dinner);
        addService(db, "Wine Pairing Experience", "Expertly selected wines to accompany your meal.", "Rs. 25,000 per person", "Fine Dining", R.drawable.wine_pairing_experience);
        addService(db, "Standard Cabana", "Includes two lounge chairs and towel service.", "Rs. 15,000 / day", "Poolside Cabana", R.drawable.standard_cabana);
        addService(db, "Deluxe Cabana", "Includes a mini-fridge with soft drinks and fruit platter.", "Rs. 24,000 / day", "Poolside Cabana", R.drawable.deluxe_cabana);
        addService(db, "VIP Cabana", "Includes dedicated butler service and premium drinks.", "Rs. 45,000 / day", "Poolside Cabana", R.drawable.vip_cabana);
        addService(db, "Family Cabana", "Larger space and kids' snacks.", "Rs. 30,000 / day", "Poolside Cabana", R.drawable.family_cabana);
        addService(db, "Sunset Cabana", "Best view for the sunset, includes a bottle of sparkling wine.", "Rs. 36,000 / evening", "Poolside Cabana", R.drawable.sunset_cabana);
        addService(db, "Snorkeling Trip", "Explore the nearby coral reefs with a guide.", "Rs. 18,000 per person", "Guided Beach Tour", R.drawable.snorkeling_trip);
        addService(db, "Dolphin Watching", "An early morning boat tour to see pods of dolphins.", "Rs. 22,500 per person", "Guided Beach Tour", R.drawable.dolphin_watching);
        addService(db, "Local Village Bike Tour", "Cycle through nearby villages and experience local culture.", "Rs. 12,000 per person", "Guided Beach Tour", R.drawable.bike_tour);
        addService(db, "Lagoon Kayaking", "A peaceful paddle through the serene mangrove lagoons.", "Rs. 15,000 per person", "Guided Beach Tour", R.drawable.lagoon_kayaking);
        addService(db, "Sunset Catamaran Cruise", "A relaxing cruise along the coast with drinks and snacks.", "Rs. 27,000 per person", "Guided Beach Tour", R.drawable.sunset_catamaran_cruise);
    }

    /**
     * Private helper to streamline adding a new service to the database.
     */
    private void addService(SQLiteDatabase db, String name, String desc, String price, String category, int imageResId) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_SERVICE_NAME, name);
        values.put(COLUMN_SERVICE_DESC, desc);
        values.put(COLUMN_SERVICE_PRICE, price);
        values.put(COLUMN_SERVICE_CATEGORY, category);
        values.put(COLUMN_SERVICE_IMAGE_RES_ID, imageResId);
        db.insert(TABLE_SERVICES, null, values);
    }

    /**
     * Retrieves a list of services based on filter, search, and sort criteria.
     * @param categoryFilter The category to filter by (e.g., "Spa Treatment").
     * @param searchQuery The text to search for in the service name.
     * @param sortOrder The order to sort the results by (e.g., "Alphabetical").
     * @return A filtered and sorted list of ServiceItem objects.
     */
    public List<ServiceItem> getFilteredAndSortedServices(String categoryFilter, String searchQuery, String sortOrder) {
        List<ServiceItem> serviceList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = "1=1";
        List<String> selectionArgs = new ArrayList<>();
        if (categoryFilter != null && !categoryFilter.equalsIgnoreCase("All Services")) {
            selection += " AND " + COLUMN_SERVICE_CATEGORY + " = ?";
            selectionArgs.add(categoryFilter);
        }
        if (searchQuery != null && !searchQuery.isEmpty()) {
            selection += " AND " + COLUMN_SERVICE_NAME + " LIKE ?";
            selectionArgs.add("%" + searchQuery + "%");
        }
        String orderBy = null;
        if ("Alphabetical".equals(sortOrder)) {
            orderBy = COLUMN_SERVICE_NAME + " ASC";
        }
        Cursor cursor = db.query(TABLE_SERVICES, null, selection, selectionArgs.toArray(new String[0]), null, null, orderBy);
        if (cursor.moveToFirst()) {
            do {
                int nameIndex = cursor.getColumnIndex(COLUMN_SERVICE_NAME);
                int descIndex = cursor.getColumnIndex(COLUMN_SERVICE_DESC);
                int priceIndex = cursor.getColumnIndex(COLUMN_SERVICE_PRICE);
                int imageIndex = cursor.getColumnIndex(COLUMN_SERVICE_IMAGE_RES_ID);
                if (nameIndex != -1 && descIndex != -1 && priceIndex != -1 && imageIndex != -1) {
                    serviceList.add(new ServiceItem(cursor.getString(nameIndex), cursor.getString(descIndex), cursor.getString(priceIndex), cursor.getInt(imageIndex)));
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        return serviceList;
    }

    //SERVICE BOOKING METHODS

    /**
     * Adds a new service appointment booking to the database.
     * @param service The ServiceItem being booked.
     * @param date The selected date for the appointment.
     * @param time The selected time for the appointment.
     */
    public void addServiceBooking(ServiceItem service, String date, String time) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_BOOKED_SERVICE_NAME, service.getName());
        values.put(COLUMN_APPOINTMENT_DATE, date);
        values.put(COLUMN_APPOINTMENT_TIME, time);
        db.insert(TABLE_SERVICE_BOOKINGS, null, values);
        db.close();
    }

    /**
     * Deletes a service booking from the database based on its ID.
     * @param serviceBookingId The ID of the service booking to delete.
     */
    public void deleteServiceBooking(int serviceBookingId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SERVICE_BOOKINGS, COLUMN_SERVICE_BOOKING_ID + " = ?", new String[]{String.valueOf(serviceBookingId)});
        db.close();
    }
}
