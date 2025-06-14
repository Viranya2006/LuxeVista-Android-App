package com.example.luxevista;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

/**
 * A simple, static activity that displays a list of current hotel promotions.
 * Its only job is to show the corresponding layout file.
 */
public class PromotionsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promotions);
    }
}