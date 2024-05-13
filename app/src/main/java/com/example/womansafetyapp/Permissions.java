package com.example.womansafetyapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class Permissions extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permissions);

        // Check if permissions are granted
        if (!checkPermissions()) {
            // If permissions are not granted, request permissions
            requestPermissions();
        } else {
            // If permissions are already granted, start MainActivity
            startMainActivity();
        }
    }

    // Method to check permissions
    private boolean checkPermissions() {
        // Check if permission is granted for location
        int locationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        // Check if permission is granted for camera
        int cameraPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);

        // Return true if both permissions are granted, false otherwise
        return locationPermission == PackageManager.PERMISSION_GRANTED &&
                cameraPermission == PackageManager.PERMISSION_GRANTED;
    }

    // Method to request permissions
    private void requestPermissions() {
        // Request permissions for location and camera
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CAMERA},
                PERMISSION_REQUEST_CODE);
    }

    // Method to start MainActivity
    private void startMainActivity() {
        Intent intent = new Intent(Permissions.this, MainActivity.class);
        startActivity(intent);
        finish(); // Finish PermissionsActivity so that user cannot go back without granting permissions
    }

    // Handle permission request results
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            // Check if all permissions are granted
            boolean allPermissionsGranted = true;
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    allPermissionsGranted = false;
                    break;
                }
            }
            // If all permissions are granted, start MainActivity
            if (allPermissionsGranted) {
                startMainActivity();
            } else {
                // Handle if permissions are not granted
                // For example, display a message or finish the activity
            }
        }
    }
}
