# **LuxeVista Resort \- Android Application**

**LuxeVista** is a comprehensive, native Android application designed for a luxury beachfront resort. It serves as a digital concierge, allowing guests to seamlessly browse accommodations, reserve exclusive in-house services, and manage their stay. The application is built entirely with native Java and the Android SDK, utilizing a local SQLite database for all data persistence.

This project was developed as a primary assessment for the Mobile Application Development module (CSE5011), demonstrating a full range of Android development skills from UI/UX design to complex database management.

## **📸 Screenshots**

<img width="1080" height="2412" alt="image" src="https://github.com/user-attachments/assets/c35d9f3f-00aa-44d5-a22f-205107cadd28" />

<img width="1080" height="2412" alt="image" src="https://github.com/user-attachments/assets/bc723c3f-99e5-426c-ae68-bddde8345679" />

<img width="1080" height="2412" alt="image" src="https://github.com/user-attachments/assets/41677dfa-c7be-494f-8d97-6338a6a0e9d2" />

<img width="1080" height="2412" alt="image" src="https://github.com/user-attachments/assets/4e6443f7-5577-48ed-8125-33681d0ed300" />

<img width="1080" height="2412" alt="image" src="https://github.com/user-attachments/assets/9408f8b6-b30c-4b47-9667-6ee6b6a181a7" />

<img width="1080" height="2412" alt="image" src="https://github.com/user-attachments/assets/9bc5a1db-cdc3-4374-8d28-00551245b9e4" />

<img width="1080" height="2412" alt="image" src="https://github.com/user-attachments/assets/b60fdeab-fda2-4d5b-be68-15b02de472de" />

## **✨ Features**

The application includes a wide array of features designed to provide a complete and luxurious user experience.

#### **👤 User Authentication & Profile Management**

* **Secure Registration:** Users can create an account with their name, email, and a contact number.  
* **Robust Validation:** Implemented validation for 10-digit contact numbers and complex passwords (requiring letters, numbers, and special characters).  
* **Session Management:** Uses SharedPreferences to maintain a user session after login.  
* **Personalized Experience:** The Home and Profile screens greet the user by their registered name.  
* **Profile Editing:** Users can view and update their name, contact info, email, and password.  
* **Preference Management:** Users can set and save their preferred travel dates.

#### **🏨 Room Booking System**

* **Dynamic Room Display:** Fetches and displays a list of all available rooms from the SQLite database.  
* **Advanced Filtering & Sorting:** Users can:  
  * **Search** for rooms by name in real-time.  
  * **Filter** the list by room type (e.g., Suite, Villa).  
  * **Sort** the list by price (Low to High or High to Low).  
* **Detailed Room View:** Each room has a dedicated details page with a larger image and full description.  
* **Date-Specific Booking:** A complete booking flow allows users to select check-in and check-out dates using a native DatePickerDialog, with validation to prevent check-out before check-in.

#### **🛎️ In-House Service Reservations**

* **Comprehensive Service Menu:** Displays 20 distinct services across 4 categories (Spa Treatment, Fine Dining, Poolside Cabanas, Guided Beach Tour).  
* **Advanced Filtering & Sorting:** Just like rooms, services can be searched by name, filtered by category, and sorted alphabetically.  
* **Service Reservation:** A full reservation flow allows users to select a specific date and time for any service.

#### **📅 Unified Booking Management**

* **Centralized History:** The "My Bookings" screen displays a single, unified list of both room bookings and service reservations.  
* **Booking Cancellation:** Users can cancel any booking or reservation with a single tap, which removes the entry from the database and updates the UI instantly.  
* **Empty State Handling:** A user-friendly message is displayed if the user has no current bookings.

#### **ℹ️ Additional Features**

* **Promotions & Attractions:** The app includes static, beautifully designed informational pages for hotel promotions and nearby attractions, accessible from the user's profile.  
* **Elegant UI/UX:** Features a custom font for headings, a warm and consistent color palette, and a professional card-based layout to create a premium user experience.

## **🛠️ Technology Stack & Architecture**

* **Language:** **Java**  
* **Platform:** **Native Android (Android SDK)**  
* **Database:** **SQLite** for all local data persistence.  
* **UI:**  
  * **XML** for creating layouts.  
  * **Material Design Components:** CardView, BottomNavigationView, SearchView, Spinner.  
  * RecyclerView for displaying dynamic and efficient lists.  
* **Architecture:**  
  * **Activities & Fragments:** The app uses a Single-Activity architecture (MainActivity) to host multiple Fragments, providing a modern and efficient navigation structure.  
  * **Data Persistence:** A robust DBHelper class extending SQLiteOpenHelper manages all database schema creation, versioning, and CRUD (Create, Read, Update, Delete) operations.  
  * **Adapters (**RecyclerView.Adapter**):** Custom adapters (RoomAdapter, ServicesAdapter, BookingAdapter) act as the bridge between the data source and the UI lists.  
  * **Data Models:** Simple Java objects (Room, ServiceItem, etc.) are used to model the application's data structure.

## **🚀 Setup and Installation**

To run this project:

1. Clone the repository to your local machine:  
   git clone https://github.com/Viranya2006/LuxeVista-Android-App.git

2. Open the project in **Android Studio**.  
3. Let Gradle sync and build the project.  
4. Run the application on an Android emulator or a physical device.

**Note:** The application uses a local SQLite database. If you make any changes to the database schema in DBHelper.java, you must increment the DATABASE\_VERSION constant and perform a clean install by uninstalling the app from the device before running it again. This will trigger the onUpgrade method and correctly rebuild the database.

