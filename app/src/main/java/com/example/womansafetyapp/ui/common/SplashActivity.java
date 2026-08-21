package com.example.womansafetyapp.ui.common;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;

import com.google.firebase.auth.FirebaseAuth;
import com.example.womansafetyapp.data.model.User;
import com.example.womansafetyapp.data.model.UserRole;
import com.example.womansafetyapp.data.repository.UserRepository;
import com.example.womansafetyapp.ui.guardian.GuardianMainActivity;
import com.example.womansafetyapp.ui.police.PoliceMainActivity;
import com.example.womansafetyapp.ui.woman.WomanMainActivity;

/**
 * Entry point. If a Firebase session exists, fetches the user's role and
 * routes straight to their dashboard. Otherwise goes to Welcome.
 */
public class SplashActivity extends AppCompatActivity {

    private static final long MIN_DISPLAY_MS = 700;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SplashScreen.installSplashScreen(this);
        super.onCreate(savedInstanceState);

        new Handler(Looper.getMainLooper()).postDelayed(this::routeUser, MIN_DISPLAY_MS);
    }

    private void routeUser() {
        String uid = FirebaseAuth.getInstance().getUid();
        if (uid == null) {
            startActivity(new Intent(this, WelcomeActivity.class));
            finish();
            return;
        }

        new UserRepository().getUserProfile(uid, new UserRepository.UserCallback() {
            @Override
            public void onSuccess(User user) {
                openDashboardForRole(user.getRoleEnum());
            }

            @Override
            public void onError(String message) {
                // Profile missing or unreachable — safest fallback is Welcome/login.
                startActivity(new Intent(SplashActivity.this, WelcomeActivity.class));
                finish();
            }
        });
    }

    private void openDashboardForRole(UserRole role) {
        Intent intent;
        if (role == UserRole.GUARDIAN) {
            intent = new Intent(this, GuardianMainActivity.class);
        } else if (role == UserRole.POLICE) {
            intent = new Intent(this, PoliceMainActivity.class);
        } else {
            intent = new Intent(this, WomanMainActivity.class);
        }
        startActivity(intent);
        finish();
    }
}
