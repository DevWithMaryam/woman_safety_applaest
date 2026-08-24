package com.example.womansafetyapp.data.repository;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.example.womansafetyapp.data.model.EmergencyContact;
import com.example.womansafetyapp.utils.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages /emergencyContacts/{womanId}/{contactId}.
 */
public class ContactRepository {

    public interface ContactsCallback {
        void onSuccess(List<EmergencyContact> contacts);
        void onError(String message);
    }

    public interface SimpleCallback {
        void onSuccess();
        void onError(String message);
    }

    private final DatabaseReference contactsRoot =
            FirebaseDatabase.getInstance().getReference(Constants.NODE_EMERGENCY_CONTACTS);

    public void addContact(@NonNull String womanId, @NonNull EmergencyContact contact, SimpleCallback callback) {
        String contactId = contactsRoot.child(womanId).push().getKey();
        if (contactId == null) {
            callback.onError("Could not generate a contact id.");
            return;
        }
        contact.setContactId(contactId);
        contactsRoot.child(womanId).child(contactId).setValue(contact)
                .addOnSuccessListener(unused -> callback.onSuccess())
                .addOnFailureListener(e -> callback.onError(safeMessage(e)));
    }

    public void removeContact(@NonNull String womanId, @NonNull String contactId, SimpleCallback callback) {
        contactsRoot.child(womanId).child(contactId).removeValue()
                .addOnSuccessListener(unused -> callback.onSuccess())
                .addOnFailureListener(e -> callback.onError(safeMessage(e)));
    }

    public void getContacts(@NonNull String womanId, ContactsCallback callback) {
        contactsRoot.child(womanId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<EmergencyContact> contacts = new ArrayList<>();
                for (DataSnapshot child : snapshot.getChildren()) {
                    EmergencyContact contact = child.getValue(EmergencyContact.class);
                    if (contact != null) contacts.add(contact);
                }
                callback.onSuccess(contacts);
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