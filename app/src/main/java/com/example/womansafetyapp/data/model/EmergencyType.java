package com.example.womansafetyapp.data.model;

/**
 * Type of emergency selected during the SOS flow.
 * Determines which roles automatically see the alert:
 *  - POLICE:   visible to Guardian AND Police
 *  - MEDICAL:  visible to Guardian only (Police not auto-notified)
 *  - GENERAL:  visible to Guardian; routed per app role logic
 */
public enum EmergencyType {
    POLICE,
    MEDICAL,
    GENERAL;

    public static EmergencyType fromString(String value) {
        if (value == null) return null;
        try {
            return EmergencyType.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    /** Whether alerts of this type should be surfaced to Police dashboards. */
    public boolean isVisibleToPolice() {
        return this == POLICE || this == GENERAL;
    }
}