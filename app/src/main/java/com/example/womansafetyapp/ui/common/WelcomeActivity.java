package com.example.womansafetyapp.ui.common;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.womansafetyapp.databinding.ActivityWelcomeBinding;

public class WelcomeActivity extends AppCompatActivity {

    private ActivityWelcomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWelcomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.buttonGetStarted.setOnClickListener(v ->
                startActivity(new Intent(this, RoleSelectionActivity.class)));
    }
}
