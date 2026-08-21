package com.example.womansafetyapp.data.repository;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.example.womansafetyapp.data.model.User;
import com.example.womansafetyapp.utils.Constants;

/**
 * Reads and writes /users/{userId} in Realtime Database.
 */
public class UserRepository {

    public interface UserCallback {
        void onSuccess(User user);
        void onError(String message);
    }

    public interface SimpleCallback {
        void onSuccess();
        void onError(String message);
    }

    private final DatabaseReference usersRef =
            FirebaseDatabase.getInstance().getReference(Constants.NODE_USERS);

    public void createUserProfile(@NonNull User user, SimpleCallback callback) {
        usersRef.child(user.getUserId()).setValue(user)
                .addOnSuccessListener(unused -> callback.onSuccess())
                .addOnFailureListener(e -> callback.onError(safeMessage(e)));
    }

    public void getUserProfile(@NonNull String userId, UserCallback callback) {
        usersRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if (user != null) {
                    callback.onSuccess(user);
                } else {
                    callback.onError("User profile not found.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onError(error.getMessage());
            }
        });
    }

    private String safeMessage(Exception e) {
        String message = e.getMessage();
        return message != null ? message : "Unable to reach the server right now.";
    }
}