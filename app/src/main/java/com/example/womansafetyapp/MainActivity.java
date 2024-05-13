package com.example.womansafetyapp;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;   
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {
    TextView signUser, signGuardian, signPolice;
    Toolbar toolbar;
    NotificationManager notification;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find TextView views by their IDs
        signUser = findViewById(R.id.sign_user);
        signGuardian = findViewById(R.id.sign_guardian);
        signPolice = findViewById(R.id.sign_police);
        GPSTracker gpsTracker = new GPSTracker(MainActivity.this);

        ImageView backButton = findViewById(R.id.backbtn);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                onBackPressed(); // This will simulate the back button press
            }
        });
//        String ChannelID = "ChannelID";
//        String ChannelName = "ChannelName";
//        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
//            NotificationChannel notificationChannel = new NotificationChannel(ChannelID,
//                    ChannelName,
//                    NotificationManager.IMPORTANCE_DEFAULT);
//            notification.createNotificationChannel(notificationChannel);
//
//            NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this, ChannelID);
//
//            builder.setSmallIcon(R.drawable.ic_launcher_background)
//                    .setContentTitle("Updates").setContentText("Updates Are ready to install");
//            notification.notify(1, builder.build());
//        } else {
//            Notification notification = new NotificationCompat.Builder(this, ChannelID)
//                    .setPriority(NotificationCompat.PRIORITY_MAX)
//                    .setContentTitle("Updates")
//                    .setContentText("Updates Are ready to install")
//                    .setAutoCancel(true)
//                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
//                    .setSmallIcon(R.drawable.ic_launcher_background)
//                    .build();
//
//            NotificationManager notificationManager = ContextCompat.getSystemService(this, NotificationManager.class);
//            if (notificationManager != null) {
//                notificationManager.notify(1, notification);
//            }
//        }


        /* Get a SensorManager instance */
        gpsTracker.getLocation();
             Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());

            LatLng position = new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude());
            List<Address> addresses = null;
            try {
                addresses = geocoder.getFromLocation(gpsTracker.getLatitude(), gpsTracker.getLongitude(), 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (addresses != null && addresses.size() > 0) {
//                address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                String city = addresses.get(0).getLocality();

                Toast.makeText(MainActivity.this, ""+city, Toast.LENGTH_SHORT).show();
            }

        // Check if permissions are granted
        if (!checkPermissions()) {
            // If permissions are not granted, start PermissionsActivity
            Intent intent = new Intent(MainActivity.this, com.example.womansafetyapp.Permissions.class);
            startActivity(intent);
            finish(); // Finish MainActivity so that user cannot go back without granting permissions
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS}, 101);
            }
        }
        // Set OnClickListener for signUser TextView
        signUser.setOnClickListener(v -> {
            // Start userlogin activity when signUser TextView is clicked
            startActivity(new Intent(MainActivity.this, userlogin.class));
        });

        // Set OnClickListener for signGuardian TextView
        signGuardian.setOnClickListener(v -> {
            // Start guardianlogin activity when signGuardian TextView is clicked
            startActivity(new Intent(MainActivity.this,guardianlogin.class));
        });

        // Set OnClickListener for signPolice TextView
        signPolice.setOnClickListener(v -> {
            // Start policelogin activity when signPolice TextView is clicked
            startActivity(new Intent(MainActivity.this,policelogin.class));
        });
    }

    // Method to check permissions
    private boolean checkPermissions() {
        // Implement your logic to check permissions here
        // Return true if permissions are granted, false otherwise
        return true; // For now, returning true for demonstration purpose
    }
}
