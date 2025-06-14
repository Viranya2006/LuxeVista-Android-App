package com.example.luxevista; // Make sure this package name matches yours

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // This line sets the layout from res/layout/activity_splash.xml
        setContentView(R.layout.activity_splash);

        // We create a Handler to run a task after a specified delay
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                // This code will execute after the delay
                // Create an Intent to start the next activity (e.g., LoginActivity or MainActivity)
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);

                // Call finish() to close the splash activity so the user can't go back to it
                finish();
            }
        }, 2000); // The delay is 2000 milliseconds (2 seconds)
    }
}