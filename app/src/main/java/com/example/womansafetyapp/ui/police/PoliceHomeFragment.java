package com.example.womansafetyapp.ui.police;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.womansafetyapp.data.model.EmergencyAlert;
import com.example.womansafetyapp.databinding.FragmentPoliceHomeBinding;
import com.example.womansafetyapp.ui.common.AlertAdapter;
import com.example.womansafetyapp.utils.MapsIntentHelper;
import com.example.womansafetyapp.utils.Resource;
import com.example.womansafetyapp.viewmodel.PoliceViewModel;

import java.util.List;

public class PoliceHomeFragment extends Fragment implements AlertAdapter.AlertActionListener {

    private FragmentPoliceHomeBinding binding;
    private PoliceViewModel viewModel;
    private AlertAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentPoliceHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(PoliceViewModel.class);
        adapter = new AlertAdapter(this);
        binding.recyclerAlerts.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerAlerts.setAdapter(adapter);

        viewModel.getAlertsState().observe(getViewLifecycleOwner(), this::handleAlerts);
        viewModel.observeAlerts();
    }

    private void handleAlerts(Resource<List<EmergencyAlert>> resource) {
        if (resource == null || !resource.isSuccess()) return;
        List<EmergencyAlert> alerts = resource.getData();
        adapter.submitList(alerts);
        boolean empty = alerts == null || alerts.isEmpty();
        binding.textNoAlerts.setVisibility(empty ? View.VISIBLE : View.GONE);
        binding.recyclerAlerts.setVisibility(empty ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onViewMap(EmergencyAlert alert) {
        MapsIntentHelper.viewLocation(requireContext(), alert.getLatitude(), alert.getLongitude(), alert.getWomanName());
    }

    @Override
    public void onNavigate(EmergencyAlert alert) {
        MapsIntentHelper.navigateTo(requireContext(), alert.getLatitude(), alert.getLongitude());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}