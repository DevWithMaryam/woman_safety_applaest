package com.example.womansafetyapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class homeguardian extends AppCompatActivity {
    TextView guardianNameTextView;
    TextView locationTextView;
    CardView notifyCard, profileCard, locationCard;
    ImageView backButton;
    FirebaseDatabase database;
    DatabaseReference locationRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homeguardin);

        // Initialize views
        guardianNameTextView = findViewById(R.id.User_accountg);
        //locationTextView = findViewById(R.id.locationCardgtext);
        notifyCard = findViewById(R.id.show_notifypCard);
        profileCard = findViewById(R.id.profileCardg);
        locationCard = findViewById(R.id.locationCardg);
        backButton = findViewById(R.id.backbtn);

        // Initialize Firebase
        database = FirebaseDatabase.getInstance();
        locationRef = database.getReference("locations");

        // Set guardian name from intent
        String guardianName = getIntent().getStringExtra("guardian");
        guardianNameTextView.setText(guardianName);

        // Set onClickListener for back button
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Set onClickListener for notification card
        notifyCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendNotification();
            }
        });

        // Set onClickListener for profile card
        profileCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(homeguardian.this, guardianprofile.class);
                intent.putExtra("guardian", guardianNameTextView.getText().toString());
                startActivity(intent);
            }
        });

        // Set onClickListener for location tracking card
        locationCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchLocationUpdates();
            }
        });
    }

    private void fetchLocationUpdates() {
        locationRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserLocation location = dataSnapshot.getValue(UserLocation.class);
                if (location != null) {
                    String locationText = "Latitude: " + location.latitude + ", Longitude: " + location.longitude;
                    locationTextView.setText(locationText);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(homeguardian.this, "Failed to fetch location", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void sendNotification() {
        Toast.makeText(this, "Notification sent", Toast.LENGTH_SHORT).show();
    }
}
