package com.maryam.womensafetyapp.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseUser;
import com.maryam.womensafetyapp.data.model.User;
import com.maryam.womensafetyapp.data.model.UserRole;
import com.maryam.womensafetyapp.data.repository.AuthRepository;
import com.maryam.womensafetyapp.data.repository.UserRepository;
import com.maryam.womensafetyapp.utils.Resource;

/**
 * Backs AuthActivity. Handles both registration (auth + profile-with-role write)
 * and login (auth + profile read), exposing Resource<User> so the UI only ever
 * needs to observe one LiveData for loading/success/error.
 */
public class AuthViewModel extends ViewModel {

    private final AuthRepository authRepository = new AuthRepository();
    private final UserRepository userRepository = new UserRepository();

    private final MutableLiveData<Resource<User>> authState = new MutableLiveData<>();

    public LiveData<Resource<User>> getAuthState() {
        return authState;
    }

    public void register(@NonNull String name, @NonNull String email, @NonNull String phone,
                         @NonNull String password, @NonNull UserRole role) {
        authState.setValue(Resource.loading());

        authRepository.register(email, password, new AuthRepository.AuthCallback() {
            @Override
            public void onSuccess(FirebaseUser firebaseUser) {
                if (firebaseUser == null) {
                    authState.setValue(Resource.error("Registration failed. Please try again."));
                    return;
                }
                User user = new User(firebaseUser.getUid(), name, email, phone, role.name());
                userRepository.createUserProfile(user, new UserRepository.SimpleCallback() {
                    @Override
                    public void onSuccess() {
                        authState.setValue(Resource.success(user));
                    }

                    @Override
                    public void onError(String message) {
                        authState.setValue(Resource.error(message));
                    }
                });
            }

            @Override
            public void onError(String message) {
                authState.setValue(Resource.error(message));
            }
        });
    }

    public void login(@NonNull String email, @NonNull String password) {
        authState.setValue(Resource.loading());

        authRepository.login(email, password, new AuthRepository.AuthCallback() {
            @Override
            public void onSuccess(FirebaseUser firebaseUser) {
                if (firebaseUser == null) {
                    authState.setValue(Resource.error("Login failed. Please try again."));
                    return;
                }
                userRepository.getUserProfile(firebaseUser.getUid(), new UserRepository.UserCallback() {
                    @Override
                    public void onSuccess(User user) {
                        authState.setValue(Resource.success(user));
                    }

                    @Override
                    public void onError(String message) {
                        authState.setValue(Resource.error(message));
                    }
                });
            }

            @Override
            public void onError(String message) {
                authState.setValue(Resource.error("Invalid email or password."));
            }
        });
    }

    public void logout() {
        authRepository.logout();
    }

    public boolean isLoggedIn() {
        return authRepository.isLoggedIn();
    }
}