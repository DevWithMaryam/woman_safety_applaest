package com.example.womansafetyapp.ui.woman;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.womansafetyapp.databinding.ItemSafetyTipBinding;

public class SafetyTipAdapter extends RecyclerView.Adapter<SafetyTipAdapter.TipViewHolder> {

    private final String[] titles;
    private final String[] descriptions;

    public SafetyTipAdapter(String[] titles, String[] descriptions) {
        this.titles = titles;
        this.descriptions = descriptions;
    }

    @NonNull
    @Override
    public TipViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemSafetyTipBinding binding = ItemSafetyTipBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new TipViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull TipViewHolder holder, int position) {
        holder.binding.textTipTitle.setText(titles[position]);
        holder.binding.textTipDescription.setText(descriptions[position]);
    }

    @Override
    public int getItemCount() {
        return titles.length;
    }

    static class TipViewHolder extends RecyclerView.ViewHolder {
        final ItemSafetyTipBinding binding;

        TipViewHolder(ItemSafetyTipBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
