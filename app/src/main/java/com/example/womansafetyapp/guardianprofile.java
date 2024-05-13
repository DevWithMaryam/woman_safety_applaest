package com.example.womansafetyapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import androidx.activity.EdgeToEdge;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class guardianprofile extends AppCompatActivity {
    private EditText newNameEditText;
    private EditText newPasswordEditText;
    private Button updateNameButton;
    private Button updatePasswordButton;
    private Button deleteAccountButton;
    private TextView guardianEmailTextView;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_guardianprofile);

        newNameEditText = findViewById(R.id.editText_new_name);
        newPasswordEditText = findViewById(R.id.editText_new_password);
        updateNameButton = findViewById(R.id.button_update_name);
        updatePasswordButton = findViewById(R.id.button_update_password);
        deleteAccountButton = findViewById(R.id.delete_account2);
        guardianEmailTextView = findViewById(R.id.textViewg_email);

        ImageView backButton = findViewById(R.id.backbtn);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                onBackPressed(); // This will simulate the back button press
            }
        });

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference("guardians");

        if (mUser != null) {
            guardianEmailTextView.setText(mUser.getEmail());
        }

        updateNameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newName = newNameEditText.getText().toString();
                if (!newName.isEmpty()) {
                    mDatabase.child(mUser.getUid()).child("name").setValue(newName);
                    Toast.makeText(guardianprofile.this, "Name updated successfully!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(guardianprofile.this, "Please enter a new name", Toast.LENGTH_SHORT).show();
                }
            }
        });

        updatePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newPassword = newPasswordEditText.getText().toString();
                if (!newPassword.isEmpty()) {
                    mUser.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(guardianprofile.this, "Password updated successfully!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(guardianprofile.this, "Failed to update password", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(guardianprofile.this, "Please enter a new password", Toast.LENGTH_SHORT).show();
                }
            }
        });

        deleteAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Delete account successfully, navigate back to login activity
                            Toast.makeText(guardianprofile.this, "Account deleted successfully!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(guardianprofile.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            // Failed to delete account
                            Toast.makeText(guardianprofile.this, "Failed to delete account", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}
