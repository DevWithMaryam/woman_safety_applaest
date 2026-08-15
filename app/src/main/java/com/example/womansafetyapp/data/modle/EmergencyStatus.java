package com.maryam.womensafetyapp.data.model;

/**
 * Lifecycle of an emergency alert, plus failure states.
 * Happy path: IDLE -> INITIATED -> LOCATION_ACQUIRED -> ACTIVE -> RESOLVED
 * Failure states are terminal for that attempt and shown to the user directly;
 * they are not written to the database unless the alert had already been created.
 */
public enum EmergencyStatus {
    IDLE,
    INITIATED,
    LOCATION_ACQUIRED,
    ACTIVE,
    RESOLVED,

    // Failure states
    LOCATION_UNAVAILABLE,
    NETWORK_ERROR,
    PERMISSION_DENIED;

    public boolean isFailure() {
        return this == LOCATION_UNAVAILABLE || this == NETWORK_ERROR || this == PERMISSION_DENIED;
    }

    public static EmergencyStatus fromString(String value) {
        if (value == null) return IDLE;
        try {
            return EmergencyStatus.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return IDLE;
        }
    }
}