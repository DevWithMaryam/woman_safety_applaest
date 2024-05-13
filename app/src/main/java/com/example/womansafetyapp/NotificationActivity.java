//package com.example.womansafetyapp;
//
//import android.Manifest;
//import android.app.Service;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.location.Location;
//import android.location.LocationListener;
//import android.location.LocationManager;
//import android.os.Bundle;
//import android.os.IBinder;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.core.app.ActivityCompat;
//
//import com.google.android.gms.location.FusedLocationProviderClient;
//import com.google.android.gms.location.LocationServices;
//import com.google.android.gms.tasks.OnSuccessListener;
//
//public class NotificationActivity extends Service implements LocationListener {
//
//    protected LocationManager locationManager;
//    boolean checkGPS = false;
//    boolean checkNetwork = false;
//    Location loc = null;
//
//    @Nullable
//    @Override
//    public IBinder onBind(Intent intent) {
//        return null;
//    }
//
//    @Override
//    public void onLocationChanged(@NonNull Location location) {
//        Toast.makeText(getApplicationContext(),
//                "Latitude: " + location.getLatitude() + " Longitude: " + location.getLongitude(),
//                Toast.LENGTH_LONG).show();
//    }
//
//    @Override
//    public void onStatusChanged(String provider, int status, Bundle extras) {}
//
//    @Override
//    public void onProviderEnabled(@NonNull String provider) {}
//
//    @Override
//    public void onProviderDisabled(@NonNull String provider) {}
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//        getLocation();
//    }
//
//    private void getLocation() {
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
//                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            return;
//        }
//
//        locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
//        checkGPS = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
//        checkNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
//
//        if (checkGPS || checkNetwork) {
//            if (checkGPS) {
//                locationManager.requestLocationUpdates(
//                        LocationManager.GPS_PROVIDER,
//                        MIN_TIME_BW_UPDATES,
//                        MIN_DISTANCE_CHANGE_FOR_UPDATES,
//                        this
//                );
//            }
//
//            if (checkNetwork) {
//                locationManager.requestLocationUpdates(
//                        LocationManager.NETWORK_PROVIDER,
//                        MIN_TIME_BW_UPDATES,
//                        MIN_DISTANCE_CHANGE_FOR_UPDATES,
//                        this
//                );
//            }
//
//            if (locationManager != null) {
//                FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
//                fusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
//                    @Override
//                    public void onSuccess(Location location) {
//                        if (location != null) {
//                            Toast.makeText(getApplicationContext(),
//                                    "Latitude: " + location.getLatitude() + " Longitude: " + location.getLongitude() + " from method",
//                                    Toast.LENGTH_LONG).show();
//                        }
//                    }
//                });
//            }
//        } else {
//            Toast.makeText(getApplicationContext(), "No provider enabled", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 100; // 100 meters
//    private static final long MIN_TIME_BW_UPDATES = 30 * 1000; // 30 seconds
//}
