package com.example.womansafetyapp.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.example.womansafetyapp.data.model.User;
import com.example.womansafetyapp.data.repository.UserRepository;
import com.example.womansafetyapp.utils.Resource;

public class WomanViewModel extends ViewModel {

    private final UserRepository userRepository = new UserRepository();
    private final MutableLiveData<Resource<User>> profileState = new MutableLiveData<>();

    public LiveData<Resource<User>> getProfileState() {
        return profileState;
    }

    public void loadProfile() {
        String uid = FirebaseAuth.getInstance().getUid();
        if (uid == null) {
            profileState.setValue(Resource.error("Not signed in."));
            return;
        }
        profileState.setValue(Resource.loading());
        userRepository.getUserProfile(uid, new UserRepository.UserCallback() {
            @Override
            public void onSuccess(User user) {
                profileState.setValue(Resource.success(user));
            }

            @Override
            public void onError(String message) {
                profileState.setValue(Resource.error(message));
            }
        });
    }
}
