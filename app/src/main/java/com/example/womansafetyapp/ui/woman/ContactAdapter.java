package com.example.womansafetyapp.ui.woman;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.womansafetyapp.data.model.EmergencyContact;
import com.example.womansafetyapp.databinding.ItemContactBinding;

import java.util.ArrayList;
import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {

    public interface ContactActionListener {
        void onCall(EmergencyContact contact);
        void onDelete(EmergencyContact contact);
    }

    private final List<EmergencyContact> contacts = new ArrayList<>();
    private final ContactActionListener listener;

    public ContactAdapter(ContactActionListener listener) {
        this.listener = listener;
    }

    public void submitList(List<EmergencyContact> newContacts) {
        contacts.clear();
        if (newContacts != null) contacts.addAll(newContacts);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemContactBinding binding = ItemContactBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ContactViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        EmergencyContact contact = contacts.get(position);
        holder.binding.textContactName.setText(contact.getName());
        holder.binding.textContactRelationship.setText(contact.getRelationship());
        holder.binding.textContactPhone.setText(contact.getPhone());
        holder.binding.buttonCallContact.setOnClickListener(v -> listener.onCall(contact));
        holder.binding.buttonDeleteContact.setOnClickListener(v -> listener.onDelete(contact));
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    static class ContactViewHolder extends RecyclerView.ViewHolder {
        final ItemContactBinding binding;

        ContactViewHolder(ItemContactBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
