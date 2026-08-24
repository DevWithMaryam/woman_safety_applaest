package com.example.womansafetyapp.ui.woman;

import android.Manifest;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.womansafetyapp.databinding.FragmentLocationBinding;
import com.example.womansafetyapp.utils.MapsIntentHelper;
import com.example.womansafetyapp.utils.Resource;
import com.example.womansafetyapp.viewmodel.LocationViewModel;

public class LocationFragment extends Fragment {

    private FragmentLocationBinding binding;
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentLocationBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(LocationViewModel.class);
        viewModel.getLocationState().observe(getViewLifecycleOwner(), this::handleState);

        binding.buttonRefreshLocation.setOnClickListener(v -> requestLocation());
        binding.buttonOpenMaps.setOnClickListener(v -> {
            if (hasFix) {
                MapsIntentHelper.viewLocation(requireContext(), lastLat, lastLng, "My Current Location");
            }
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
        binding.buttonOpenMaps.setEnabled(resource.isSuccess());

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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
