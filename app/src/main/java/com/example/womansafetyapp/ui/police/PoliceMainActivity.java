package com.example.womansafetyapp.ui.police;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.womansafetyapp.R;
import com.example.womansafetyapp.databinding.ActivityPoliceMainBinding;

/** Hosts Police's two fragments (Alerts, Profile) via bottom navigation. */
public class PoliceMainActivity extends AppCompatActivity {

    private ActivityPoliceMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPoliceMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (savedInstanceState == null) {
            showFragment(new PoliceHomeFragment());
        }

        binding.bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.navAlerts) {
                showFragment(new PoliceHomeFragment());
                return true;
            } else if (id == R.id.navProfile) {
                showFragment(new PoliceProfileFragment());
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