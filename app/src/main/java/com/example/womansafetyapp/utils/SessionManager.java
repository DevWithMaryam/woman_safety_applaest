package com.example.womansafetyapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.womansafetyapp.data.model.UserRole;

/**
 * Lightweight local cache of the signed-in user's role, so app restart can
 * route straight to the correct dashboard without waiting on a database read.
 * Firebase Authentication itself remains the source of truth for the session;
 * this class only avoids an extra round trip on cold start.
 */
public class SessionManager {

    private static final String PREFS_NAME = "women_safety_session";
    private static final String KEY_ROLE = "key_role";

    private final SharedPreferences prefs;

    public SessionManager(Context context) {
        prefs = context.getApplicationContext()
                .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public void saveRole(UserRole role) {
        prefs.edit().putString(KEY_ROLE, role.name()).apply();
    }

    public UserRole getCachedRole() {
        return UserRole.fromString(prefs.getString(KEY_ROLE, null));
    }

    public void clear() {
        prefs.edit().clear().apply();
    }
}