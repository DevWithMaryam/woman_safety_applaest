package com.example.womansafetyapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    // Constant for location permission request code
    private static final int FINE_PERMISSION_CODE = 1;

    // GoogleMap instance
    private GoogleMap myMap;

    // Variables for storing current location and location services
    private Location currentLocation;
    private FusedLocationProviderClient fusedLocationProviderClient;

    // SearchView for searching locations on the map
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Initializing FusedLocationProviderClient to get the user's location
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        getLastLocation();

        // Initializing SearchView and setting up a listener for search queries
        searchView = findViewById(R.id.sv_location);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Search for the location when the query is submitted
                searchLocation(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    // Method to get the user's last known location
    private void getLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Requesting location permission if not granted
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_PERMISSION_CODE);
            return;
        }
        // Getting the last known location
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    // Storing the current location
                    currentLocation = location;

                    // Initializing the map fragment and setting up the map asynchronously
                    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                    if (mapFragment != null) {
                        mapFragment.getMapAsync(MapsActivity.this);
                    } else {
                        Log.e("MapsActivity", "Map Fragment is null!");
                    }
                } else {
                    Log.e("MapsActivity", "Location is null!");
                }
            }
        });
    }

    // Method called when the map is ready to be used
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        myMap = googleMap;

        // Enabling user location on the map with appropriate permissions
        try {
            myMap.setMyLocationEnabled(true);
        } catch (SecurityException se) {
            Log.e("MapsActivity", "SecurityException: " + se.getMessage());
        }

        // Setting up various map features
        myMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        myMap.setTrafficEnabled(true);
        myMap.setIndoorEnabled(true);
        myMap.setBuildingsEnabled(true);
        myMap.getUiSettings().setZoomControlsEnabled(true);
        myMap.getUiSettings().setCompassEnabled(true);
        myMap.getUiSettings().setZoomGesturesEnabled(true);
        myMap.getUiSettings().setScrollGesturesEnabled(true);

        // Adding a marker for the user's current location and moving the camera
        if (currentLocation != null) {
            LatLng userLocation = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
            myMap.addMarker(new MarkerOptions().position(userLocation).title("My Location"));
            myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15));
        } else {
            // If no location is found, use a default location (Lahore, Pakistan)
            LatLng defaultLocation = new LatLng(31.5497, 74.3436); // Lahore, Pakistan
            myMap.addMarker(new MarkerOptions().position(defaultLocation).title("Default Location"));
            myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 15));
        }
    }

    // Method to search for a location based on user input
    private void searchLocation(String location) {
        Geocoder geocoder = new Geocoder(this);
        List<Address> addresses;

        try {
            if (currentLocation != null) {
                // Limiting the search within a bounding box around the current location
                addresses = geocoder.getFromLocationName(location, 5,
                        currentLocation.getLatitude() - 0.05, currentLocation.getLongitude() - 0.05,
                        currentLocation.getLatitude() + 0.05, currentLocation.getLongitude() + 0.05);
            } else {
                // Performing a global search if current location is not available
                addresses = geocoder.getFromLocationName(location, 1);
            }

            if (addresses.size() > 0) {
                // Adding a marker at the found location and moving the camera
                LatLng latLng = new LatLng(addresses.get(0).getLatitude(), addresses.get(0).getLongitude());
                myMap.addMarker(new MarkerOptions().position(latLng).title(location));
                myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
            } else {
                Toast.makeText(this, "Location not found", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error finding location", Toast.LENGTH_SHORT).show();
        }
    }

    // Handling the result of the location permission request
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == FINE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // If permission granted, get the last location
                getLastLocation();
            } else {
                Toast.makeText(this, "Location permission is denied, please allow the permission", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
