package com.example.womansafetyapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.womansafetyapp.location.SafetyLocationManager;
import com.example.womansafetyapp.utils.Resource;

/**
 * data[0] = latitude, data[1] = longitude on success.
 * AndroidViewModel is used (not plain ViewModel) because SafetyLocationManager needs a Context.
 */
public class LocationViewModel extends AndroidViewModel {

    private final SafetyLocationManager locationManager;
    private final MutableLiveData<Resource<double[]>> locationState = new MutableLiveData<>();

    public LocationViewModel(@NonNull Application application) {
        super(application);
        locationManager = new SafetyLocationManager(application);
    }

    public LiveData<Resource<double[]>> getLocationState() {
        return locationState;
    }

    public void fetchCurrentLocation() {
        locationState.setValue(Resource.loading());

        locationManager.getCurrentLocation(new SafetyLocationManager.LocationCallback() {
            @Override
            public void onLocationResult(double latitude, double longitude) {
                locationState.setValue(Resource.success(new double[]{latitude, longitude}));
            }

            @Override
            public void onPermissionDenied() {
                locationState.setValue(Resource.error("Location permission is required to send an accurate alert."));
            }

            @Override
            public void onLocationDisabled() {
                locationState.setValue(Resource.error("Location services are turned off. Please enable GPS."));
            }

            @Override
            public void onLocationUnavailable() {
                locationState.setValue(Resource.error("Unable to determine your current location."));
            }
        });
    }

    public boolean hasPermission() {
        return locationManager.hasLocationPermission();
    }
}
