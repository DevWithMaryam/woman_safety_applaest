package com.example.womansafetyapp.ui.woman;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.womansafetyapp.data.model.User;
import com.example.womansafetyapp.databinding.FragmentWomanHomeBinding;
import com.example.womansafetyapp.ui.common.EmergencyActivity;
import com.example.womansafetyapp.utils.Resource;
import com.example.womansafetyapp.viewmodel.WomanViewModel;

public class WomanHomeFragment extends Fragment {

    private FragmentWomanHomeBinding binding;
    private WomanViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentWomanHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(WomanViewModel.class);
        viewModel.getProfileState().observe(getViewLifecycleOwner(), this::handleProfile);
        viewModel.loadProfile();

        // SOS is the core action — Phase 4 implements the full confirmation/type/location flow.
        binding.buttonSos.setOnClickListener(v ->
                startActivity(new Intent(requireContext(), EmergencyActivity.class)));

        binding.cardContacts.setOnClickListener(v -> {
            // Emergency contacts management lives inside ProfileFragment for now.
            navigateToNav(com.example.womansafetyapp.R.id.navProfile);
        });

        binding.cardLocation.setOnClickListener(v -> navigateToNav(com.example.womansafetyapp.R.id.navLocation));

        binding.cardNearbyHelp.setOnClickListener(v ->
                startActivity(new Intent(requireContext(), com.example.womansafetyapp.ui.common.LocationActivity.class)));

        binding.cardSafetyTips.setOnClickListener(v -> navigateToNav(com.example.womansafetyapp.R.id.navTips));
    }

    private void navigateToNav(int menuItemId) {
        if (getActivity() instanceof WomanMainActivity) {
            com.google.android.material.bottomnavigation.BottomNavigationView bottomNav =
                    getActivity().findViewById(com.example.womansafetyapp.R.id.bottomNav);
            if (bottomNav != null) {
                bottomNav.setSelectedItemId(menuItemId);
            }
        }
    }

    private void handleProfile(Resource<User> resource) {
        if (resource != null && resource.isSuccess() && resource.getData() != null) {
            String greeting = getString(com.example.womansafetyapp.R.string.greeting_prefix)
                    + " " + resource.getData().getName();
            binding.textGreeting.setText(greeting);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}