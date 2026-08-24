package com.example.womansafetyapp.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;
import com.example.womansafetyapp.data.model.User;
import com.example.womansafetyapp.data.model.UserRole;
import com.example.womansafetyapp.databinding.ActivityAuthBinding;
import com.example.womansafetyapp.ui.guardian.GuardianMainActivity;
import com.example.womansafetyapp.ui.police.PoliceMainActivity;
import com.example.womansafetyapp.ui.woman.WomanMainActivity;
import com.example.womansafetyapp.utils.Constants;
import com.example.womansafetyapp.utils.Resource;
import com.example.womansafetyapp.utils.SessionManager;
import com.example.womansafetyapp.viewmodel.AuthViewModel;

/**
 * Single Activity for both Login and Register (Phase 4 rule: no separate
 * per-role signup screens). Mode is toggled in-place; role only matters
 * for registration and arrives via Constants.EXTRA_ROLE.
 */
public class AuthActivity extends AppCompatActivity {

    private ActivityAuthBinding binding;
    private AuthViewModel viewModel;
    private SessionManager sessionManager;

    private boolean isRegisterMode = false;
    private UserRole pendingRole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAuthBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        sessionManager = new SessionManager(this);

        binding.buttonBack.setOnClickListener(v -> finish());

        String roleExtra = getIntent().getStringExtra(Constants.EXTRA_ROLE);
        pendingRole = UserRole.fromString(roleExtra);
        isRegisterMode = pendingRole != null;
        applyMode();

        binding.textToggleMode.setOnClickListener(v -> {
            isRegisterMode = !isRegisterMode;
            applyMode();
        });

        binding.buttonSubmit.setOnClickListener(v -> submit());

        viewModel.getAuthState().observe(this, this::handleAuthState);
    }

    private void applyMode() {
        if (isRegisterMode) {
            binding.textAuthTitle.setText(getString(com.example.womansafetyapp.R.string.register));
            binding.buttonSubmit.setText(getString(com.example.womansafetyapp.R.string.register));
            binding.textToggleMode.setText(getString(com.example.womansafetyapp.R.string.have_account_prompt));
            binding.layoutName.setVisibility(View.VISIBLE);
            binding.layoutPhone.setVisibility(View.VISIBLE);
            binding.layoutConfirmPassword.setVisibility(View.VISIBLE);
        } else {
            binding.textAuthTitle.setText(getString(com.example.womansafetyapp.R.string.login));
            binding.buttonSubmit.setText(getString(com.example.womansafetyapp.R.string.login));
            binding.textToggleMode.setText(getString(com.example.womansafetyapp.R.string.no_account_prompt));
            binding.layoutName.setVisibility(View.GONE);
            binding.layoutPhone.setVisibility(View.GONE);
            binding.layoutConfirmPassword.setVisibility(View.GONE);
        }
    }

    private void submit() {
        String email = text(binding.layoutEmail);
        String password = text(binding.layoutPassword);

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            showError("Please fill in email and password.");
            return;
        }

        if (isRegisterMode) {
            String name = text(binding.layoutName);
            String phone = text(binding.layoutPhone);
            String confirm = text(binding.layoutConfirmPassword);

            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(phone)) {
                showError("Please fill in all fields.");
                return;
            }
            if (!password.equals(confirm)) {
                showError("Passwords do not match.");
                return;
            }
            if (pendingRole == null) {
                showError("Please select a role first.");
                return;
            }
            viewModel.register(name, email, phone, password, pendingRole);
        } else {
            viewModel.login(email, password);
        }
    }

    private void handleAuthState(Resource<User> resource) {
        if (resource == null) return;

        binding.progressBar.setVisibility(resource.isLoading() ? View.VISIBLE : View.GONE);
        binding.buttonSubmit.setEnabled(!resource.isLoading());

        if (resource.isSuccess() && resource.getData() != null) {
            User user = resource.getData();
            UserRole role = user.getRoleEnum();
            if (role != null) {
                sessionManager.saveRole(role);
            }
            openDashboard(role);
        } else if (resource.isError()) {
            showError(resource.getMessage());
        }
    }

    private void openDashboard(UserRole role) {
        Intent intent;
        if (role == UserRole.GUARDIAN) {
            intent = new Intent(this, GuardianMainActivity.class);
        } else if (role == UserRole.POLICE) {
            intent = new Intent(this, PoliceMainActivity.class);
        } else {
            intent = new Intent(this, WomanMainActivity.class);
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private String text(com.google.android.material.textfield.TextInputLayout layout) {
        return layout.getEditText() != null ? layout.getEditText().getText().toString().trim() : "";
    }

    private void showError(String message) {
        Snackbar.make(binding.getRoot(), message != null ? message : "Something went wrong.",
                Snackbar.LENGTH_LONG).show();
    }
}