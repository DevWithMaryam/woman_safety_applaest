package com.example.womansafetyapp;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class userlogin extends AppCompatActivity {
    EditText loginUsername, loginPassword;
    Button loginButton;
    TextView signupRedirectText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_userlogin);
        loginUsername = findViewById(R.id.ul_username);
        loginPassword = findViewById(R.id.ul_password);
        signupRedirectText = findViewById(R.id.newU_account);
        loginButton = findViewById(R.id.button1); // Corrected button ID

        ImageView backButton = findViewById(R.id.backbtn);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                onBackPressed(); // This will simulate the back button press
            }
        });

        loginButton.setOnClickListener(v -> {
            if (!validateUsername() | !validatePassword()) {

            } else {
                checkUser();
            }
        });
        signupRedirectText.setOnClickListener(v -> {
            Intent intent = new Intent(userlogin.this, userSignup.class);
            startActivity(intent);
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

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
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
                        String signupGmailFromDB = snapshot.child(userUsername).child("signupGmail").getValue(String.class);
                        String EmergencyNumberFromDB = snapshot.child(userUsername).child("emergencyNumber").getValue(String.class);//singupGmail use to get guardian gmail


                        // Create an Intent to start the MainActivity
                        Intent intent = new Intent(userlogin.this, homeactivity.class);

                        // Pass user information as extras to the intent
                        intent.putExtra("name", nameFromDB);
                        intent.putExtra("email", emailFromDB);
                        intent.putExtra("username", usernameFromDB);
                        intent.putExtra("password", PasswordFromDB);
                        intent.putExtra("signupGmail", signupGmailFromDB);
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