package com.example.womansafetyapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.example.womansafetyapp.data.model.EmergencyAlert;
import com.example.womansafetyapp.data.model.EmergencyStatus;
import com.example.womansafetyapp.data.model.EmergencyType;
import com.example.womansafetyapp.data.repository.EmergencyRepository;
import com.example.womansafetyapp.location.SafetyLocationManager;
import com.example.womansafetyapp.utils.Resource;

/**
 * Drives the full SOS flow described in Phase 6/7 of the spec:
 * type selection -> location acquisition -> alert creation -> status updates.
 * EmergencyActivity only observes emergencyState and calls startEmergency()/resolve().
 */
public class EmergencyViewModel extends AndroidViewModel {

    private final EmergencyRepository emergencyRepository = new EmergencyRepository();
    private final SafetyLocationManager locationManager;

    private final MutableLiveData<Resource<EmergencyAlert>> emergencyState = new MutableLiveData<>();
    private final MutableLiveData<EmergencyStatus> statusState = new MutableLiveData<>(EmergencyStatus.IDLE);

    public EmergencyViewModel(@NonNull Application application) {
        super(application);
        locationManager = new SafetyLocationManager(application);
    }

    public LiveData<Resource<EmergencyAlert>> getEmergencyState() { return emergencyState; }
    public LiveData<EmergencyStatus> getStatusState() { return statusState; }

    /** Call once the user has confirmed the countdown and picked an emergency type. */
    public void startEmergency(@NonNull EmergencyType type, @NonNull String womanName) {
        String uid = FirebaseAuth.getInstance().getUid();
        if (uid == null) {
            emergencyState.setValue(Resource.error("Not signed in."));
            statusState.setValue(EmergencyStatus.IDLE);
            return;
        }

        statusState.setValue(EmergencyStatus.INITIATED);
        emergencyState.setValue(Resource.loading());

        locationManager.getCurrentLocation(new SafetyLocationManager.LocationCallback() {
            @Override
            public void onLocationResult(double latitude, double longitude) {
                statusState.setValue(EmergencyStatus.LOCATION_ACQUIRED);
                createAlert(uid, womanName, type, latitude, longitude);
            }

            @Override
            public void onPermissionDenied() {
                statusState.setValue(EmergencyStatus.PERMISSION_DENIED);
                emergencyState.setValue(Resource.error("Location permission is required to send an accurate alert."));
            }

            @Override
            public void onLocationDisabled() {
                statusState.setValue(EmergencyStatus.LOCATION_UNAVAILABLE);
                emergencyState.setValue(Resource.error("Location services are turned off. Please enable GPS."));
            }

            @Override
            public void onLocationUnavailable() {
                statusState.setValue(EmergencyStatus.LOCATION_UNAVAILABLE);
                emergencyState.setValue(Resource.error("Unable to determine your current location."));
            }
        });
    }

    private void createAlert(String uid, String womanName, EmergencyType type, double lat, double lng) {
        EmergencyAlert alert = new EmergencyAlert(
                null, uid, womanName, type.name(), lat, lng,
                System.currentTimeMillis(), EmergencyStatus.ACTIVE.name());

        emergencyRepository.createAlert(alert, new EmergencyRepository.AlertCallback() {
            @Override
            public void onSuccess(EmergencyAlert created) {
                statusState.setValue(EmergencyStatus.ACTIVE);
                emergencyState.setValue(Resource.success(created));
            }

            @Override
            public void onError(String message) {
                statusState.setValue(EmergencyStatus.NETWORK_ERROR);
                emergencyState.setValue(Resource.error(
                        message != null ? message : "Could not create the emergency alert. Please try again."));
            }
        });
    }

    /** Marks the current alert resolved (used from the status screen once safe). */
    public void resolveAlert(@NonNull String alertId) {
        emergencyRepository.updateStatus(alertId, EmergencyStatus.RESOLVED, new EmergencyRepository.SimpleCallback() {
            @Override
            public void onSuccess() {
                statusState.setValue(EmergencyStatus.RESOLVED);
            }

            @Override
            public void onError(String message) {
                emergencyState.setValue(Resource.error(message));
            }
        });
    }

    public void reset() {
        statusState.setValue(EmergencyStatus.IDLE);
    }
}