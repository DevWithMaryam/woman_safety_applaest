package com.example.womansafetyapp.ui.common;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.womansafetyapp.data.model.UserRole;
import com.example.womansafetyapp.databinding.ActivityRoleSelectionBinding;
import com.example.womansafetyapp.ui.auth.AuthActivity;
import com.example.womansafetyapp.utils.Constants;

public class RoleSelectionActivity extends AppCompatActivity {

    private ActivityRoleSelectionBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRoleSelectionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.buttonBack.setOnClickListener(v -> finish());

        binding.cardWoman.setOnClickListener(v -> openAuth(UserRole.WOMAN));
        binding.cardGuardian.setOnClickListener(v -> openAuth(UserRole.GUARDIAN));
        binding.cardPolice.setOnClickListener(v -> openAuth(UserRole.POLICE));
    }

    private void openAuth(UserRole role) {
        Intent intent = new Intent(this, AuthActivity.class);
        intent.putExtra(Constants.EXTRA_ROLE, role.name());
        startActivity(intent);
    }
}