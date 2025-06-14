package com.example.luxevista;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

/**
 * The initial splash screen of the application.
 * This activity displays the app's brand for a short period
 * before automatically navigating to the LoginActivity.
 */
public class SplashActivity extends AppCompatActivity {

    // The duration the splash screen will be visible, in milliseconds.
    private static final int SPLASH_DELAY = 2000; // 2 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // A Handler is used to schedule a task to be run after a delay.
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                // This code will execute after the SPLASH_DELAY has passed.
                // Create an Intent to start the LoginActivity.
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);

                // Call finish() to close the SplashActivity so the user cannot go back to it.
                finish();
            }
        }, SPLASH_DELAY);
    }
}
