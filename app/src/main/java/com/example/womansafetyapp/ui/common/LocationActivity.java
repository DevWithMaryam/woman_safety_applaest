package com.example.womansafetyapp.ui.common;

import android.Manifest;
import android.os.Bundle;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.womansafetyapp.databinding.ActivityLocationBinding;
import com.example.womansafetyapp.utils.MapsIntentHelper;
import com.example.womansafetyapp.utils.Resource;
import com.example.womansafetyapp.viewmodel.LocationViewModel;

/**
 * Nearby Help screen: acquires current location, then lets the user search
 * nearby police stations or hospitals via Google Maps intents (no paid SDK).
 */
public class LocationActivity extends AppCompatActivity {

    private ActivityLocationBinding binding;
    private LocationViewModel viewModel;
    private double lastLat, lastLng;
    private boolean hasFix = false;

    private final ActivityResultLauncher<String[]> permissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> {
                boolean granted = Boolean.TRUE.equals(result.get(Manifest.permission.ACCESS_FINE_LOCATION))
                        || Boolean.TRUE.equals(result.get(Manifest.permission.ACCESS_COARSE_LOCATION));
                if (granted) {
                    viewModel.fetchCurrentLocation();
                } else {
                    showError("Location permission is required to send an accurate alert.");
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLocationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(LocationViewModel.class);
        viewModel.getLocationState().observe(this, this::handleState);

        binding.buttonClose.setOnClickListener(v -> finish());

        binding.buttonNearbyPolice.setOnClickListener(v -> {
            if (hasFix) MapsIntentHelper.searchNearby(this, "police station", lastLat, lastLng);
        });

        binding.buttonNearbyHospital.setOnClickListener(v -> {
            if (hasFix) MapsIntentHelper.searchNearby(this, "hospital", lastLat, lastLng);
        });

        requestLocation();
    }

    private void requestLocation() {
        if (viewModel.hasPermission()) {
            viewModel.fetchCurrentLocation();
        } else {
            permissionLauncher.launch(new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            });
        }
    }

    private void handleState(Resource<double[]> resource) {
        if (resource == null) return;

        binding.progressLocation.setVisibility(resource.isLoading() ? View.VISIBLE : View.GONE);
        binding.textLocationError.setVisibility(resource.isError() ? View.VISIBLE : View.GONE);
        binding.buttonNearbyPolice.setEnabled(resource.isSuccess());
        binding.buttonNearbyHospital.setEnabled(resource.isSuccess());

        if (resource.isSuccess() && resource.getData() != null) {
            lastLat = resource.getData()[0];
            lastLng = resource.getData()[1];
            hasFix = true;
            binding.textCoordinates.setText(String.format("%.5f, %.5f", lastLat, lastLng));
        } else if (resource.isError()) {
            showError(resource.getMessage());
        }
    }

    private void showError(String message) {
        binding.textLocationError.setVisibility(View.VISIBLE);
        binding.textLocationError.setText(message);
    }
}