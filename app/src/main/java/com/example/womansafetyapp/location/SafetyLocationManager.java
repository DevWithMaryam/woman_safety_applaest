package com.maryam.womensafetyapp.location;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.CancellationSignal;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.CurrentLocationRequest;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;

/**
 * Wraps FusedLocationProviderClient to fetch a single fresh current location
 * on demand (no continuous background tracking, per project scope).
 */
public class SafetyLocationManager {

    public interface LocationCallback {
        void onLocationResult(double latitude, double longitude);
        void onPermissionDenied();
        void onLocationDisabled();
        void onLocationUnavailable();
    }

    private final Context context;
    private final FusedLocationProviderClient fusedClient;

    public SafetyLocationManager(Context context) {
        this.context = context.getApplicationContext();
        this.fusedClient = LocationServices.getFusedLocationProviderClient(this.context);
    }

    public boolean hasLocationPermission() {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;
    }

    public boolean isLocationEnabled() {
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return lm != null && (lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER));
    }

    public void getCurrentLocation(LocationCallback callback) {
        if (!hasLocationPermission()) {
            callback.onPermissionDenied();
            return;
        }
        if (!isLocationEnabled()) {
            callback.onLocationDisabled();
            return;
        }

        CurrentLocationRequest request = new CurrentLocationRequest.Builder()
                .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
                .build();

        try {
            fusedClient.getCurrentLocation(request, new CancellationSignal())
                    .addOnSuccessListener(location -> {
                        if (location != null) {
                            callback.onLocationResult(location.getLatitude(), location.getLongitude());
                        } else {
                            callback.onLocationUnavailable();
                        }
                    })
                    .addOnFailureListener(e -> callback.onLocationUnavailable());
        } catch (SecurityException e) {
            callback.onPermissionDenied();
        }
    }
}