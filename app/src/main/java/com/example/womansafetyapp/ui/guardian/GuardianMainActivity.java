package com.example.womansafetyapp.ui.guardian;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.womansafetyapp.R;
import com.example.womansafetyapp.databinding.ActivityGuardianMainBinding;

/** Hosts Guardian's two fragments (Alerts, Profile) via bottom navigation. */
public class GuardianMainActivity extends AppCompatActivity {

    private ActivityGuardianMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGuardianMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (savedInstanceState == null) {
            showFragment(new GuardianHomeFragment());
        }

        binding.bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.navAlerts) {
                showFragment(new GuardianHomeFragment());
                return true;
            } else if (id == R.id.navProfile) {
                showFragment(new GuardianProfileFragment());
                return true;
            }
            return false;
        });
    }

    private void showFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit();
    }
}