package com.example.luxevista;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Set the listener for the navigation bar
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;

                // Using a switch statement is cleaner for multiple items
                int itemId = item.getItemId();
                if (itemId == R.id.nav_home) {
                    selectedFragment = new HomeFragment();
                } else if (itemId == R.id.nav_rooms) {
                    selectedFragment = new RoomsFragment();
                } else if (itemId == R.id.nav_services) {
                    selectedFragment = new ServicesFragment();
                } else if (itemId == R.id.nav_bookings) {
                    selectedFragment = new BookingsFragment();


                } else if (itemId == R.id.nav_profile) {
                    selectedFragment = new ProfileFragment();
                }

                if (selectedFragment != null) {
                    loadFragment(selectedFragment);
                }
                return true; // Return true to display the item as the selected item
            }
        });

        // Load the HomeFragment by default when the activity is first created
        if (savedInstanceState == null) {
            bottomNavigationView.setSelectedItemId(R.id.nav_home); // This will trigger the listener
        }
    }

    /**
     * Helper method to replace the current fragment in the container.
     *
     * @param fragment The fragment to display.
     */
    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Replace the existing fragment with the new one
        fragmentTransaction.replace(R.id.fragment_container, fragment);

        // Commit the transaction
        fragmentTransaction.commit();
    }
}