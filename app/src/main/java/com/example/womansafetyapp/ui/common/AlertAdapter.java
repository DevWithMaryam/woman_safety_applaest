package com.example.womansafetyapp.ui.common;

import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.womansafetyapp.data.model.EmergencyAlert;
import com.example.womansafetyapp.databinding.ItemAlertBinding;

import java.util.ArrayList;
import java.util.List;

public class AlertAdapter extends RecyclerView.Adapter<AlertAdapter.AlertViewHolder> {

    public interface AlertActionListener {
        void onViewMap(EmergencyAlert alert);
        void onNavigate(EmergencyAlert alert);
    }

    private final List<EmergencyAlert> alerts = new ArrayList<>();
    private final AlertActionListener listener;

    public AlertAdapter(AlertActionListener listener) {
        this.listener = listener;
    }

    public void submitList(List<EmergencyAlert> newAlerts) {
        alerts.clear();
        if (newAlerts != null) alerts.addAll(newAlerts);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AlertViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemAlertBinding binding = ItemAlertBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new AlertViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AlertViewHolder holder, int position) {
        EmergencyAlert alert = alerts.get(position);
        holder.binding.textAlertWomanName.setText(alert.getWomanName());
        holder.binding.textAlertType.setText("Type: " + alert.getType());
        holder.binding.textAlertStatus.setText(alert.getStatus());
        holder.binding.textAlertTime.setText(
                DateUtils.getRelativeTimeSpanString(alert.getTimestamp()));

        int statusColor;
        if (alert.getStatusEnum() == com.example.womansafetyapp.data.model.EmergencyStatus.RESOLVED) {
            statusColor = androidx.core.content.ContextCompat.getColor(
                    holder.itemView.getContext(), com.example.womansafetyapp.R.color.status_resolved);
        } else if (alert.getStatusEnum() == com.example.womansafetyapp.data.model.EmergencyStatus.ACTIVE) {
            statusColor = androidx.core.content.ContextCompat.getColor(
                    holder.itemView.getContext(), com.example.womansafetyapp.R.color.status_active);
        } else {
            statusColor = androidx.core.content.ContextCompat.getColor(
                    holder.itemView.getContext(), com.example.womansafetyapp.R.color.status_pending);
        }
        holder.binding.textAlertStatus.setBackgroundColor(statusColor);

        holder.binding.buttonViewMap.setOnClickListener(v -> listener.onViewMap(alert));
        holder.binding.buttonNavigate.setOnClickListener(v -> listener.onNavigate(alert));
    }

    @Override
    public int getItemCount() {
        return alerts.size();
    }

    static class AlertViewHolder extends RecyclerView.ViewHolder {
        final ItemAlertBinding binding;

        AlertViewHolder(ItemAlertBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}