package com.example.womansafetyapp.utils;

import android.app.Application;

import com.google.firebase.FirebaseApp;

/**
 * Application entry point. Initializes Firebase once for the whole process.
 */
public class WomenSafetyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
    }
}