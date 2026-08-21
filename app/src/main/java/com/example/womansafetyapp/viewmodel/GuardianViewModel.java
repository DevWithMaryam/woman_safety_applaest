package com.example.womansafetyapp.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.ValueEventListener;
import com.example.womansafetyapp.data.model.EmergencyAlert;
import com.example.womansafetyapp.data.repository.EmergencyRepository;
import com.example.womansafetyapp.utils.Resource;

import java.util.ArrayList;
import java.util.List;

public class GuardianViewModel extends ViewModel {

    private final EmergencyRepository emergencyRepository = new EmergencyRepository();
    private final MutableLiveData<Resource<List<EmergencyAlert>>> alertsState = new MutableLiveData<>();
    private ValueEventListener listener;

    public LiveData<Resource<List<EmergencyAlert>>> getAlertsState() {
        return alertsState;
    }

    public void observeAlerts() {
        alertsState.setValue(Resource.loading());
        listener = emergencyRepository.observeAlerts(new EmergencyRepository.AlertsListCallback() {
            @Override
            public void onSuccess(List<EmergencyAlert> alerts) {
                List<EmergencyAlert> sorted = new ArrayList<>(alerts);
                sorted.sort((a, b) -> Long.compare(b.getTimestamp(), a.getTimestamp()));
                alertsState.setValue(Resource.success(sorted));
            }

            @Override
            public void onError(String message) {
                alertsState.setValue(Resource.error(message));
            }
        });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (listener != null) {
            emergencyRepository.removeObserver(listener);
        }
    }
}