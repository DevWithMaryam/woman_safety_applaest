package com.example.womansafetyapp;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class homeactivity extends AppCompatActivity {
    CardView alertCard, alarmCard, locationCard, safetyCard, mapCard, profileCard;
    TextView usernameTextView;

    GPSTracker gpsTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homeactivity);

        // Initialize GPS Tracker
        gpsTracker = new GPSTracker(this);

        // Initialize Views
        alertCard = findViewById(R.id.alertCard);
        alarmCard = findViewById(R.id.alarmCard);
        locationCard = findViewById(R.id.locationCard);
        safetyCard = findViewById(R.id.safteyCard);
        mapCard = findViewById(R.id.mapCard);
        profileCard = findViewById(R.id.profileCard);
        usernameTextView = findViewById(R.id.UserN_account);

        // Set username received from previous activity
        String username = getIntent().getStringExtra("username");
        usernameTextView.setText(username);

        mapCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(homeactivity.this, MapsActivity.class);
                startActivity(intent);
            }
        });

        alertCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendAlertNotifications();
            }
        });

        alarmCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final MediaPlayer mediaPlayer = MediaPlayer.create(homeactivity.this, R.raw.bell);
                mediaPlayer.start();
            }
        });

        safetyCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(homeactivity.this, safety_tips.class));
            }
        });

        profileCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(homeactivity.this, user_profile.class);
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });

        // Set onClickListener for back button
        ImageView backButton = findViewById(R.id.backbtn);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                onBackPressed(); // This will simulate the back button press
            }
        });
    }

    private void sendAlertNotifications() {
        // Your code to send alert notifications
        Toast.makeText(this, "Alert Notification Sent!", Toast.LENGTH_SHORT).show();
    }

    private void startLocationUpdates() {
        // Add your code to start location updates here
    }
}
