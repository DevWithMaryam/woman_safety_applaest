package com.example.womansafetyapp.data.model;

/**
 * The three supported user roles in the application.
 * Stored as a String on the user's Realtime Database profile.
 */
public enum UserRole {
    WOMAN,
    GUARDIAN,
    POLICE;

    public static UserRole fromString(String value) {
        if (value == null) return null;
        try {
            return UserRole.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}