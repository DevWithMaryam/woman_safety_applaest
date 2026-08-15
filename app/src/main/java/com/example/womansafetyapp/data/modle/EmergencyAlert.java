package com.maryam.womensafetyapp.data.model;

/**
 * Maps to: /emergencyAlerts/{alertId}
 * "type" and "status" are stored as Strings for RTDB compatibility and
 * converted to enums via the fromString() helpers when read.
 */
public class EmergencyAlert {

    private String alertId;
    private String womanId;
    private String womanName;
    private String type;     // EmergencyType
    private double latitude;
    private double longitude;
    private long timestamp;
    private String status;   // EmergencyStatus

    public EmergencyAlert() {
        // Required empty constructor for Firebase
    }

    public EmergencyAlert(String alertId, String womanId, String womanName, String type,
                          double latitude, double longitude, long timestamp, String status) {
        this.alertId = alertId;
        this.womanId = womanId;
        this.womanName = womanName;
        this.type = type;
        this.latitude = latitude;
        this.longitude = longitude;
        this.timestamp = timestamp;
        this.status = status;
    }

    public String getAlertId() { return alertId; }
    public void setAlertId(String alertId) { this.alertId = alertId; }

    public String getWomanId() { return womanId; }
    public void setWomanId(String womanId) { this.womanId = womanId; }

    public String getWomanName() { return womanName; }
    public void setWomanName(String womanName) { this.womanName = womanName; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }

    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public EmergencyType getTypeEnum() { return EmergencyType.fromString(type); }
    public EmergencyStatus getStatusEnum() { return EmergencyStatus.fromString(status); }
}