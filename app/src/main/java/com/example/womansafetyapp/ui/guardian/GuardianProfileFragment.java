package com.example.womansafetyapp.ui.guardian;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.womansafetyapp.R;
import com.example.womansafetyapp.data.model.User;
import com.example.womansafetyapp.databinding.FragmentGuardianProfileBinding;
import com.example.womansafetyapp.ui.common.WelcomeActivity;
import com.example.womansafetyapp.utils.Resource;
import com.example.womansafetyapp.utils.SessionManager;
import com.example.womansafetyapp.viewmodel.AuthViewModel;
import com.example.womansafetyapp.viewmodel.WomanViewModel;

public class GuardianProfileFragment extends Fragment {

    private FragmentGuardianProfileBinding binding;
    private WomanViewModel profileViewModel; // reused: same "fetch signed-in user" logic
    private AuthViewModel authViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentGuardianProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        profileViewModel = new ViewModelProvider(this).get(WomanViewModel.class);
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        profileViewModel.getProfileState().observe(getViewLifecycleOwner(), this::handleProfile);
        profileViewModel.loadProfile();

        binding.buttonLogout.setOnClickListener(v -> confirmLogout());
    }

    private void handleProfile(Resource<User> resource) {
        if (resource != null && resource.isSuccess() && resource.getData() != null) {
            User user = resource.getData();
            binding.textProfileName.setText(user.getName());
            binding.textProfileEmail.setText(user.getEmail());
            binding.textProfilePhone.setText(user.getPhone());
            binding.textProfileRole.setText(getString(R.string.role_guardian));
        }
    }

    private void confirmLogout() {
        new AlertDialog.Builder(requireContext())
                .setTitle(getString(R.string.logout))
                .setMessage("Are you sure you want to log out?")
                .setPositiveButton(getString(R.string.logout), (dialog, which) -> {
                    authViewModel.logout();
                    new SessionManager(requireContext()).clear();
                    Intent intent = new Intent(requireContext(), WelcomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    requireActivity().finish();
                })
                .setNegativeButton(getString(R.string.cancel), null)
                .show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}