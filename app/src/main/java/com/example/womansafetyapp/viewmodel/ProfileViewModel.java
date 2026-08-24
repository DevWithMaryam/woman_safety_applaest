package com.example.womansafetyapp.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.example.womansafetyapp.data.model.EmergencyContact;
import com.example.womansafetyapp.data.model.User;
import com.example.womansafetyapp.data.repository.ContactRepository;
import com.example.womansafetyapp.data.repository.UserRepository;
import com.example.womansafetyapp.utils.Resource;

import java.util.List;

public class ProfileViewModel extends ViewModel {

    private final UserRepository userRepository = new UserRepository();
    private final ContactRepository contactRepository = new ContactRepository();

    private final MutableLiveData<Resource<User>> profileState = new MutableLiveData<>();
    private final MutableLiveData<Resource<List<EmergencyContact>>> contactsState = new MutableLiveData<>();
    private final MutableLiveData<Resource<Void>> contactActionState = new MutableLiveData<>();

    public LiveData<Resource<User>> getProfileState() { return profileState; }
    public LiveData<Resource<List<EmergencyContact>>> getContactsState() { return contactsState; }
    public LiveData<Resource<Void>> getContactActionState() { return contactActionState; }

    public void loadProfile() {
        String uid = currentUid();
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

    public void loadContacts() {
        String uid = currentUid();
        if (uid == null) {
            contactsState.setValue(Resource.error("Not signed in."));
            return;
        }
        contactsState.setValue(Resource.loading());
        contactRepository.getContacts(uid, new ContactRepository.ContactsCallback() {
            @Override
            public void onSuccess(List<EmergencyContact> contacts) {
                contactsState.setValue(Resource.success(contacts));
            }

            @Override
            public void onError(String message) {
                contactsState.setValue(Resource.error(message));
            }
        });
    }

    public void addContact(String name, String phone, String relationship) {
        String uid = currentUid();
        if (uid == null) {
            contactActionState.setValue(Resource.error("Not signed in."));
            return;
        }
        contactActionState.setValue(Resource.loading());
        EmergencyContact contact = new EmergencyContact(null, name, phone, relationship);
        contactRepository.addContact(uid, contact, new ContactRepository.SimpleCallback() {
            @Override
            public void onSuccess() {
                contactActionState.setValue(Resource.success(null));
                loadContacts();
            }

            @Override
            public void onError(String message) {
                contactActionState.setValue(Resource.error(message));
            }
        });
    }

    public void deleteContact(String contactId) {
        String uid = currentUid();
        if (uid == null) return;
        contactRepository.removeContact(uid, contactId, new ContactRepository.SimpleCallback() {
            @Override
            public void onSuccess() {
                loadContacts();
            }

            @Override
            public void onError(String message) {
                contactActionState.setValue(Resource.error(message));
            }
        });
    }

    private String currentUid() {
        return FirebaseAuth.getInstance().getUid();
    }
}
