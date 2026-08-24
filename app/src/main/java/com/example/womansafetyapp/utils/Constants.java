package com.example.womansafetyapp.utils;

/**
 * Central place for Realtime Database node names and shared constants,
 * so paths are never hard-coded inline across repositories.
 */
public final class Constants {

    private Constants() { }

    // Realtime Database root nodes
    public static final String NODE_USERS = "users";
    public static final String NODE_EMERGENCY_CONTACTS = "emergencyContacts";
    public static final String NODE_EMERGENCY_ALERTS = "emergencyAlerts";

    // Intent extras
    public static final String EXTRA_ROLE = "extra_role";
    public static final String EXTRA_ALERT_ID = "extra_alert_id";
    public static final String EXTRA_LATITUDE = "extra_latitude";
    public static final String EXTRA_LONGITUDE = "extra_longitude";

    // SOS confirmation countdown, in seconds
    public static final int SOS_COUNTDOWN_SECONDS = 3;
}