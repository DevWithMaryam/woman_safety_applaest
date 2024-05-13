package com.example.womansafetyapp;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class userSignup extends AppCompatActivity {

    EditText signupUname, signupEmail, sigupUsername, signupPassword, signupGmail,phonNumber;
    Button signup;
    TextView backlogin;
    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_signup);
        signupUname = findViewById(R.id.us_name);
        signupEmail = findViewById(R.id.us_email);
        sigupUsername = findViewById(R.id.us_username);
        signupPassword = findViewById(R.id.us_password);
        signupGmail = findViewById(R.id.us_guardianemail);
        phonNumber = findViewById(R.id.us_phonnumber);
        backlogin = findViewById(R.id.back_login); // Corrected variable name
        signup = findViewById(R.id.button1); // Assigning to signup button



        ImageView backButton = findViewById(R.id.backbtn);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                onBackPressed(); // This will simulate the back button press
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database = FirebaseDatabase.getInstance();
                reference = database.getReference("users");
                String name = signupUname.getText().toString();
                String email = signupEmail.getText().toString();
                String username = sigupUsername.getText().toString();
                String password = signupPassword.getText().toString();// Get the text from signupGmail EditText
                String guardianEmail = signupGmail.getText().toString();
                String EmergencyNumber = phonNumber.getText().toString();

// Create HelperClass object with the retrieved guardian email
                HelperClass helperClass = new HelperClass(name, email, username, password, guardianEmail, EmergencyNumber);

                reference.child(username).setValue(helperClass);

                Toast.makeText(userSignup.this, "You have signed up successfully!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(userSignup.this, userlogin.class);
                startActivity(intent);
            }
        });

        // Set onClickListener for back to login text
        backlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to login activity
                Intent intent = new Intent(userSignup.this, userlogin.class);
                startActivity(intent);
            }
        });
    }
}