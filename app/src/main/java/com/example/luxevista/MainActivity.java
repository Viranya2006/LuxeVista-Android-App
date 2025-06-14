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

/**
 * The main container Activity that hosts all the primary fragments of the app.
 * It manages the BottomNavigationView and handles the swapping of fragments
 * as the user navigates through the app.
 */
public class MainActivity extends AppCompatActivity {

    // The BottomNavigationView UI element from the layout.
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Link the bottomNavigationView variable to the view in the XML layout.
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Set the listener that will be triggered when a navigation item is selected.
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;

                // Determine which fragment to display based on the ID of the selected menu item.
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

                // If a valid fragment has been selected, load it into the container.
                if (selectedFragment != null) {
                    loadFragment(selectedFragment);
                }
                // Return true to highlight the selected item in the navigation bar.
                return true;
            }
        });

        // This ensures that the HomeFragment is loaded as the default screen
        // when the activity is first created.
        if (savedInstanceState == null) {
            bottomNavigationView.setSelectedItemId(R.id.nav_home);
        }
    }

    /**
     * A helper method to handle the replacement of fragments within the FrameLayout container.
     * @param fragment The fragment to be displayed.
     */
    private void loadFragment(Fragment fragment) {
        // Get the FragmentManager to handle fragment transactions.
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Replace the content of the 'fragment_container' with the new fragment.
        fragmentTransaction.replace(R.id.fragment_container, fragment);

        // Commit the transaction to apply the changes.
        fragmentTransaction.commit();
    }
}
