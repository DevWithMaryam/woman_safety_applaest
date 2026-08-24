package com.example.womansafetyapp.ui.woman;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.womansafetyapp.R;
import com.example.womansafetyapp.databinding.FragmentSafetyTipsBinding;

public class SafetyTipsFragment extends Fragment {

    private FragmentSafetyTipsBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentSafetyTipsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String[] titles = getResources().getStringArray(R.array.safety_tips_titles);
        String[] descriptions = getResources().getStringArray(R.array.safety_tips_descriptions);

        binding.recyclerSafetyTips.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerSafetyTips.setAdapter(new SafetyTipAdapter(titles, descriptions));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
