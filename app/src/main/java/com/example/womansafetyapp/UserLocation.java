package com.example.womansafetyapp;

public class UserLocation {
    public double latitude;
    public double longitude;

    public UserLocation() {
        // Default constructor required for calls to DataSnapshot.getValue(UserLocation.class)
    }

    public UserLocation(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
