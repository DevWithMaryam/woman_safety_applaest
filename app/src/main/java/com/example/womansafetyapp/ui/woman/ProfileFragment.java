package com.example.womansafetyapp.ui.woman;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.example.womansafetyapp.data.model.EmergencyContact;
import com.example.womansafetyapp.data.model.User;
import com.example.womansafetyapp.databinding.DialogAddContactBinding;
import com.example.womansafetyapp.databinding.FragmentProfileBinding;
import com.example.womansafetyapp.ui.common.WelcomeActivity;
import com.example.womansafetyapp.utils.Resource;
import com.example.womansafetyapp.utils.SessionManager;
import com.example.womansafetyapp.viewmodel.AuthViewModel;
import com.example.womansafetyapp.viewmodel.ProfileViewModel;

import java.util.List;

public class ProfileFragment extends Fragment implements ContactAdapter.ContactActionListener {

    private FragmentProfileBinding binding;
    private ProfileViewModel viewModel;
    private AuthViewModel authViewModel;
    private ContactAdapter contactAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        contactAdapter = new ContactAdapter(this);
        binding.recyclerContacts.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerContacts.setAdapter(contactAdapter);

        viewModel.getProfileState().observe(getViewLifecycleOwner(), this::handleProfile);
        viewModel.getContactsState().observe(getViewLifecycleOwner(), this::handleContacts);
        viewModel.getContactActionState().observe(getViewLifecycleOwner(), this::handleContactAction);

        viewModel.loadProfile();
        viewModel.loadContacts();

        binding.buttonAddContact.setOnClickListener(v -> showAddContactDialog());
        binding.buttonLogout.setOnClickListener(v -> confirmLogout());
    }

    private void handleProfile(Resource<User> resource) {
        if (resource != null && resource.isSuccess() && resource.getData() != null) {
            User user = resource.getData();
            binding.textProfileName.setText(user.getName());
            binding.textProfileEmail.setText(user.getEmail());
            binding.textProfilePhone.setText(user.getPhone());
        }
    }

    private void handleContacts(Resource<List<EmergencyContact>> resource) {
        if (resource == null) return;
        if (resource.isSuccess()) {
            List<EmergencyContact> contacts = resource.getData();
            contactAdapter.submitList(contacts);
            boolean empty = contacts == null || contacts.isEmpty();
            binding.textNoContacts.setVisibility(empty ? View.VISIBLE : View.GONE);
            binding.recyclerContacts.setVisibility(empty ? View.GONE : View.VISIBLE);
        } else if (resource.isError()) {
            showSnackbar(resource.getMessage());
        }
    }

    private void handleContactAction(Resource<Void> resource) {
        if (resource != null && resource.isError()) {
            showSnackbar(resource.getMessage());
        }
    }

    private void showAddContactDialog() {
        DialogAddContactBinding dialogBinding =
                DialogAddContactBinding.inflate(LayoutInflater.from(requireContext()));

        new AlertDialog.Builder(requireContext())
                .setTitle("Add Emergency Contact")
                .setView(dialogBinding.getRoot())
                .setPositiveButton("Add", (dialog, which) -> {
                    String name = textOf(dialogBinding.layoutContactName);
                    String phone = textOf(dialogBinding.layoutContactPhone);
                    String relationship = textOf(dialogBinding.layoutContactRelationship);

                    if (TextUtils.isEmpty(name) || TextUtils.isEmpty(phone)) {
                        showSnackbar("Name and phone are required.");
                        return;
                    }
                    viewModel.addContact(name, phone, relationship);
                })
                .setNegativeButton(getString(com.example.womansafetyapp.R.string.cancel), null)
                .show();
    }

    private String textOf(TextInputLayout layout) {
        TextInputEditText editText = (TextInputEditText) layout.getEditText();
        return editText != null ? editText.getText().toString().trim() : "";
    }

    private void confirmLogout() {
        new AlertDialog.Builder(requireContext())
                .setTitle(getString(com.example.womansafetyapp.R.string.logout))
                .setMessage("Are you sure you want to log out?")
                .setPositiveButton(getString(com.example.womansafetyapp.R.string.logout), (dialog, which) -> {
                    authViewModel.logout();
                    new SessionManager(requireContext()).clear();
                    Intent intent = new Intent(requireContext(), WelcomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    requireActivity().finish();
                })
                .setNegativeButton(getString(com.example.womansafetyapp.R.string.cancel), null)
                .show();
    }

    @Override
    public void onCall(EmergencyContact contact) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + contact.getPhone()));
        startActivity(intent);
    }

    @Override
    public void onDelete(EmergencyContact contact) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Remove Contact")
                .setMessage("Remove " + contact.getName() + " from your emergency contacts?")
                .setPositiveButton("Remove", (dialog, which) -> viewModel.deleteContact(contact.getContactId()))
                .setNegativeButton(getString(com.example.womansafetyapp.R.string.cancel), null)
                .show();
    }

    private void showSnackbar(String message) {
        if (binding != null) {
            Snackbar.make(binding.getRoot(), message != null ? message : "Something went wrong.",
                    Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
