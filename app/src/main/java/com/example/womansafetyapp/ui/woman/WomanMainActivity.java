package com.example.womansafetyapp.ui.woman;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.womansafetyapp.R;
import com.example.womansafetyapp.databinding.ActivityWomanMainBinding;

/**
 * Hosts the four Woman-role fragments (Home, Location, Safety Tips, Profile)
 * switched via BottomNavigationView, per the "small number of Activities" rule.
 */
public class WomanMainActivity extends AppCompatActivity {

    private ActivityWomanMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWomanMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (savedInstanceState == null) {
            showFragment(new WomanHomeFragment());
        }

        binding.bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.navHome) {
                showFragment(new WomanHomeFragment());
                return true;
            } else if (id == R.id.navLocation) {
                showFragment(new LocationFragment());
                return true;
            } else if (id == R.id.navTips) {
                showFragment(new SafetyTipsFragment());
                return true;
            } else if (id == R.id.navProfile) {
                showFragment(new ProfileFragment());
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
