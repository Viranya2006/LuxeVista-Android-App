package com.example.luxevista;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

/**
 * A simple, static activity that displays a list of nearby attractions and tours.
 * Its only job is to show the corresponding layout file.
 */
public class AttractionsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attractions);
    }
}