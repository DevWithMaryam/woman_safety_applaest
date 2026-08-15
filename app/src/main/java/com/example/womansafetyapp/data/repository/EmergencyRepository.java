package com.maryam.womensafetyapp.data.repository;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.maryam.womensafetyapp.data.model.EmergencyAlert;
import com.maryam.womensafetyapp.data.model.EmergencyStatus;
import com.maryam.womensafetyapp.utils.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages /emergencyAlerts/{alertId}.
 *
 * Visibility rule (Phase 7/9): Police dashboards filter out MEDICAL-only alerts
 * client-side via EmergencyType#isVisibleToPolice(). Guardian dashboards see all
 * alerts belonging to the women they are linked to (linkage/filtering by woman
 * is applied by the caller/ViewModel, not this repository).
 */
public class EmergencyRepository {

    public interface AlertCallback {
        void onSuccess(EmergencyAlert alert);
        void onError(String message);
    }

    public interface AlertsListCallback {
        void onSuccess(List<EmergencyAlert> alerts);
        void onError(String message);
    }

    public interface SimpleCallback {
        void onSuccess();
        void onError(String message);
    }

    private final DatabaseReference alertsRef =
            FirebaseDatabase.getInstance().getReference(Constants.NODE_EMERGENCY_ALERTS);

    /** Creates a new alert with a generated id and INITIATED status; returns the created alert. */
    public void createAlert(@NonNull EmergencyAlert alert, AlertCallback callback) {
        String alertId = alertsRef.push().getKey();
        if (alertId == null) {
            callback.onError("Could not generate an alert id.");
            return;
        }
        alert.setAlertId(alertId);
        alertsRef.child(alertId).setValue(alert)
                .addOnSuccessListener(unused -> callback.onSuccess(alert))
                .addOnFailureListener(e -> callback.onError(safeMessage(e)));
    }

    public void updateStatus(@NonNull String alertId, @NonNull EmergencyStatus status, SimpleCallback callback) {
        alertsRef.child(alertId).child("status").setValue(status.name())
                .addOnSuccessListener(unused -> callback.onSuccess())
                .addOnFailureListener(e -> callback.onError(safeMessage(e)));
    }

    /**
     * One-shot fetch of all alerts. Guardian/Police ViewModels apply their own
     * relevance filtering (linked woman / EmergencyType#isVisibleToPolice()).
     */
    public void getAllAlerts(AlertsListCallback callback) {
        alertsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<EmergencyAlert> alerts = new ArrayList<>();
                for (DataSnapshot child : snapshot.getChildren()) {
                    EmergencyAlert alert = child.getValue(EmergencyAlert.class);
                    if (alert != null) alerts.add(alert);
                }
                callback.onSuccess(alerts);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onError(error.getMessage());
            }
        });
    }

    /** Live observer so Guardian/Police screens update as alerts change. Caller must detach the listener. */
    public ValueEventListener observeAlerts(AlertsListCallback callback) {
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<EmergencyAlert> alerts = new ArrayList<>();
                for (DataSnapshot child : snapshot.getChildren()) {
                    EmergencyAlert alert = child.getValue(EmergencyAlert.class);
                    if (alert != null) alerts.add(alert);
                }
                callback.onSuccess(alerts);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onError(error.getMessage());
            }
        };
        alertsRef.addValueEventListener(listener);
        return listener;
    }

    public void removeObserver(ValueEventListener listener) {
        alertsRef.removeEventListener(listener);
    }

    private String safeMessage(Exception e) {
        String message = e.getMessage();
        return message != null ? message : "Unable to reach the server right now.";
    }
}