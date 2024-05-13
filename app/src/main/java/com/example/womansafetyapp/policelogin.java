package com.example.womansafetyapp;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import android.graphics.drawable.ColorDrawable;


public class policelogin extends AppCompatActivity {
    EditText loginUsername, loginPassword;
    Button loginButton;
    TextView signupRedirectText;
    Button forgotPassword;           // line 1 reset password

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_policelogin);
        loginUsername = findViewById(R.id.pl_email);
        loginPassword = findViewById(R.id.pl_password);
        signupRedirectText = findViewById(R.id.pcreate_account);
//        phonNumber = findViewById(R.id.police_no);
        loginButton = findViewById(R.id.button3); // Corrected button ID
        forgotPassword = findViewById(R.id.forgot_password);        // line 2 reset password

        loginButton.setOnClickListener(v -> {
            if (!validateUsername() | !validatePassword()) {

            } else {
                checkUser();
            }
        });
        signupRedirectText.setOnClickListener(v -> {
            Intent intent = new Intent(policelogin.this, policesignup.class);
            startActivity(intent);
        });
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                 final Dialog dialog = new Dialog(policelogin.this);
//                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                dialog.setCancelable(false);
//                dialog.setContentView(R.layout.dialog_forgot);
//                EditText emailBox = dialog.findViewById(R.id.emailBox);
//
//
//                Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);
//                btnCancel.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        dialog.dismiss();
//                    }
//                });
//                Button btnReset = (Button) dialog.findViewById(R.id.btnReset);
//                btnReset.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        String userEmail = emailBox.getText().toString();
//
//                        if (TextUtils.isEmpty(userEmail) && !Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()) {
//                            Toast.makeText(policelogin.this, "Enter your registered email id", Toast.LENGTH_SHORT).show();
//                            return;
//                        }
//
//                        FirebaseAuth.getInstance().sendPasswordResetEmail(userEmail)
//                                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<Void> task) {
//                                        if (task.isSuccessful()) {
//                                            if (task.isSuccessful()) {
//                                                Toast.makeText(policelogin.this, "Check your email", Toast.LENGTH_SHORT).show();
//                                                dialog.dismiss();
//                                            } else {
//                                                Toast.makeText(policelogin.this, "Unable to send, failed", Toast.LENGTH_SHORT).show();
//                                            }
//                                        }
//                                    }
//                                });
//                        dialog.dismiss();
//                    }
//                });
//
//                dialog.show();


                AlertDialog.Builder builder = new AlertDialog.Builder(policelogin.this);
                View dialogView = getLayoutInflater().inflate(R.layout.dialog_forgot, null);
                EditText emailBox = dialogView.findViewById(R.id.emailBox);

                builder.setView(dialogView);
                AlertDialog dialog = builder.create();

                dialogView.findViewById(R.id.btnReset).setOnClickListener(view12 -> {
                    String userEmail = emailBox.getText().toString();

                    if (TextUtils.isEmpty(userEmail) && !Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()){
                        Toast.makeText(policelogin.this, "Enter your registered email id", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    FirebaseAuth.getInstance().sendPasswordResetEmail(userEmail)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        if (task.isSuccessful()){
                                Toast.makeText(policelogin.this, "Check your email", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            } else {
                                Toast.makeText(policelogin.this, "Unable to send, failed", Toast.LENGTH_SHORT).show();
                            }
                                    }
                                }
                            });

//                    reference.toString ().compareTo(new OnCompleteListener<Void>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Void> task) {
//                            if (task.isSuccessful()){
//                                Toast.makeText(policelogin.this, "Check your email", Toast.LENGTH_SHORT).show();
//                                dialog.dismiss();
//                            } else {
//                                Toast.makeText(policelogin.this, "Unable to send, failed", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    }.toString());
                });
                dialogView.findViewById(R.id.btnCancel).setOnClickListener(view1 -> dialog.dismiss());
                if (dialog.getWindow() != null){
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                }
                dialog.show();
            }
        });

    }

    public Boolean validateUsername() {
        String val = loginUsername.getText().toString();
        if (val.isEmpty()) {
            loginUsername.setError("username cannot be empty");
            return false;
        } else {
            loginUsername.setError(null);
            return true;
        }
    }

    public Boolean validatePassword() {
        String val = loginPassword.getText().toString();
        if (val.isEmpty()) {
            loginPassword.setError("password cannot be empty");
            return false;
        } else {
            loginPassword.setError(null);
            return true;
        }
    }

    public void checkUser() {
        String userUsername = loginUsername.getText().toString().trim();
        String userPassword = loginPassword.getText().toString().trim();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("police");
        Query checkUserDatabase = reference.orderByChild("username").equalTo(userUsername);

        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    loginUsername.setError(null);

                    // Retrieve password from the database
                    String passwordFromDB = snapshot.child(userUsername).child("password").getValue(String.class);

                    if (passwordFromDB != null && passwordFromDB.equals(userPassword)) {
                        // Clear any previous error
                        loginPassword.setError(null);

                        // Retrieve other user information from the database
                        String nameFromDB = snapshot.child(userUsername).child("name").getValue(String.class);
                        String emailFromDB = snapshot.child(userUsername).child("email").getValue(String.class);
                        String usernameFromDB = snapshot.child(userUsername).child("username").getValue(String.class);
                        String PasswordFromDB = snapshot.child(userUsername).child("password").getValue(String.class);
                        String EmergencyNumberFromDB = snapshot.child(userUsername).child("emergencyNumber").getValue(String.class);


                        // Create an Intent to start the MainActivity
                        Intent intent = new Intent(policelogin.this, homepolice.class);

                        // Pass user information as extras to the intent
                        intent.putExtra("name", nameFromDB);
                        intent.putExtra("email", emailFromDB);
                        intent.putExtra("username", usernameFromDB);
                        intent.putExtra("password", passwordFromDB);
                        intent.putExtra("emergencyNumber", EmergencyNumberFromDB);


                        // Start the MainActivity
                        startActivity(intent);
                    } else {
                        // Show an error if the password is incorrect
                        loginPassword.setError("Invalid Password");
                        loginPassword.requestFocus();
                    }
                } else {
                    // Show an error if the user does not exist
                    loginUsername.setError("User does not exist");
                    loginUsername.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });

    }
}