package com.maryam.womensafetyapp.data.repository;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Wraps Firebase Authentication (email/password only).
 * No UI or Activity/Fragment code should call FirebaseAuth directly.
 */
public class AuthRepository {

    public interface AuthCallback {
        void onSuccess(FirebaseUser user);
        void onError(String message);
    }

    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    public void register(@NonNull String email, @NonNull String password, AuthCallback callback) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(result -> callback.onSuccess(result.getUser()))
                .addOnFailureListener(e -> callback.onError(mapError(e)));
    }

    public void login(@NonNull String email, @NonNull String password, AuthCallback callback) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(result -> callback.onSuccess(result.getUser()))
                .addOnFailureListener(e -> callback.onError(mapError(e)));
    }

    public void logout() {
        firebaseAuth.signOut();
    }

    public FirebaseUser getCurrentUser() {
        return firebaseAuth.getCurrentUser();
    }

    public boolean isLoggedIn() {
        return firebaseAuth.getCurrentUser() != null;
    }

    private String mapError(Exception e) {
        // Firebase already returns reasonably user-safe messages; fall back to a generic one.
        String message = e.getMessage();
        return message != null ? message : "Authentication failed. Please try again.";
    }
}