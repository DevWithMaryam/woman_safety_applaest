package com.example.womansafetyapp.ui.common;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.example.womansafetyapp.R;
import com.example.womansafetyapp.data.model.EmergencyAlert;
import com.example.womansafetyapp.data.model.EmergencyStatus;
import com.example.womansafetyapp.data.model.EmergencyType;
import com.example.womansafetyapp.data.model.User;
import com.example.womansafetyapp.data.repository.UserRepository;
import com.example.womansafetyapp.databinding.ActivityEmergencyBinding;
import com.example.womansafetyapp.databinding.BottomsheetEmergencyTypeBinding;
import com.example.womansafetyapp.databinding.DialogSosConfirmBinding;
import com.example.womansafetyapp.utils.Constants;
import com.example.womansafetyapp.utils.MapsIntentHelper;
import com.example.womansafetyapp.utils.Resource;
import com.example.womansafetyapp.viewmodel.EmergencyViewModel;

/**
 * Full SOS flow: confirmation countdown -> type selection -> location acquisition
 * (handled inside EmergencyViewModel) -> alert creation -> status screen.
 * Launched directly from WomanHomeFragment's SOS button.
 */
public class EmergencyActivity extends AppCompatActivity {

    private ActivityEmergencyBinding binding;
    private EmergencyViewModel viewModel;
    private EmergencyAlert activeAlert;
    private String womanName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEmergencyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(EmergencyViewModel.class);
        viewModel.getEmergencyState().observe(this, this::handleEmergencyState);
        viewModel.getStatusState().observe(this, this::handleStatus);

        binding.buttonClose.setOnClickListener(v -> finish());
        binding.buttonRetry.setOnClickListener(v -> showConfirmDialog());

        loadNameThenConfirm();
    }

    private void loadNameThenConfirm() {
        String uid = FirebaseAuth.getInstance().getUid();
        if (uid == null) {
            finish();
            return;
        }
        new UserRepository().getUserProfile(uid, new UserRepository.UserCallback() {
            @Override
            public void onSuccess(User user) {
                womanName = user.getName() != null ? user.getName() : "";
                showConfirmDialog();
            }

            @Override
            public void onError(String message) {
                womanName = "";
                showConfirmDialog();
            }
        });
    }

    private void showConfirmDialog() {
        DialogSosConfirmBinding dialogBinding = DialogSosConfirmBinding.inflate(LayoutInflater.from(this));
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(dialogBinding.getRoot())
                .setCancelable(false)
                .create();
        dialog.show();

        CountDownTimer timer = new CountDownTimer(Constants.SOS_COUNTDOWN_SECONDS * 1000L, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int secondsLeft = (int) Math.ceil(millisUntilFinished / 1000.0);
                dialogBinding.textCountdown.setText(String.valueOf(secondsLeft));
            }

            @Override
            public void onFinish() {
                dialog.dismiss();
                showTypeSelection();
            }
        };
        timer.start();

        dialogBinding.buttonCancelSos.setOnClickListener(v -> {
            timer.cancel();
            dialog.dismiss();
            finish();
        });
    }

    private void showTypeSelection() {
        BottomsheetEmergencyTypeBinding sheetBinding =
                BottomsheetEmergencyTypeBinding.inflate(LayoutInflater.from(this));
        BottomSheetDialog sheet = new BottomSheetDialog(this);
        sheet.setContentView(sheetBinding.getRoot());
        sheet.setCancelable(false);
        sheet.show();

        sheetBinding.optionPolice.setOnClickListener(v -> {
            sheet.dismiss();
            viewModel.startEmergency(EmergencyType.POLICE, womanName);
        });
        sheetBinding.optionMedical.setOnClickListener(v -> {
            sheet.dismiss();
            viewModel.startEmergency(EmergencyType.MEDICAL, womanName);
        });
        sheetBinding.optionGeneral.setOnClickListener(v -> {
            sheet.dismiss();
            viewModel.startEmergency(EmergencyType.GENERAL, womanName);
        });
    }

    private void handleStatus(EmergencyStatus status) {
        if (status == null) return;

        binding.buttonRetry.setVisibility(View.GONE);
        binding.buttonViewOnMap.setVisibility(View.GONE);
        binding.buttonResolve.setVisibility(View.GONE);
        binding.progressEmergency.setVisibility(View.GONE);
        binding.textStatusMessage.setText("");

        switch (status) {
            case INITIATED:
                binding.textStatusTitle.setText(R.string.status_initiated);
                binding.progressEmergency.setVisibility(View.VISIBLE);
                break;
            case LOCATION_ACQUIRED:
                binding.textStatusTitle.setText(R.string.status_location_acquired);
                binding.progressEmergency.setVisibility(View.VISIBLE);
                break;
            case ACTIVE:
                binding.textStatusTitle.setText(R.string.status_active);
                binding.textStatusMessage.setText("Your alert has been sent. Stay safe — help has been notified.");
                binding.buttonViewOnMap.setVisibility(View.VISIBLE);
                binding.buttonResolve.setVisibility(View.VISIBLE);
                break;
            case RESOLVED:
                binding.textStatusTitle.setText(R.string.status_resolved);
                binding.textStatusMessage.setText("Marked as resolved. Glad you're safe.");
                break;
            case PERMISSION_DENIED:
                binding.textStatusTitle.setText(R.string.error_location_permission_denied);
                binding.buttonRetry.setVisibility(View.VISIBLE);
                break;
            case LOCATION_UNAVAILABLE:
                binding.textStatusTitle.setText(R.string.error_location_unavailable);
                binding.buttonRetry.setVisibility(View.VISIBLE);
                break;
            case NETWORK_ERROR:
                binding.textStatusTitle.setText(R.string.error_emergency_creation_failed);
                binding.buttonRetry.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }

    private void handleEmergencyState(Resource<EmergencyAlert> resource) {
        if (resource == null) return;

        if (resource.isSuccess() && resource.getData() != null) {
            activeAlert = resource.getData();
            binding.buttonViewOnMap.setOnClickListener(v -> MapsIntentHelper.viewLocation(
                    this, activeAlert.getLatitude(), activeAlert.getLongitude(), womanName));
            binding.buttonResolve.setOnClickListener(v -> viewModel.resolveAlert(activeAlert.getAlertId()));
        } else if (resource.isError()) {
            binding.textStatusMessage.setText(resource.getMessage());
        }
    }
}