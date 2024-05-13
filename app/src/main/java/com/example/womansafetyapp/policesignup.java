package com.example.womansafetyapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class policesignup extends AppCompatActivity {

    EditText signupName, signupEmail, signupUsername, signupPassword, signupnum;
    Button signupButton;
    GPSTracker gpsTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_policesignup);
        gpsTracker = new GPSTracker(policesignup.this);

        signupName = findViewById(R.id.ps_name);
        signupEmail = findViewById(R.id.ps_email);
        signupUsername = findViewById(R.id.ps_username);
        signupPassword = findViewById(R.id.ps_password);
        signupButton = findViewById(R.id.button3);
        signupnum = findViewById(R.id.police_no);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = signupName.getText().toString();
                String email = signupEmail.getText().toString();
                String username = signupUsername.getText().toString();
                String password = signupPassword.getText().toString();
                String policenum = signupnum.getText().toString();


                // Validate input fields
                if (name.isEmpty() || email.isEmpty() || username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(policesignup.this, "All fields are required", Toast.LENGTH_SHORT).show();
                } else {


                    // Create Police object with user information
                    HelperClassp helperClassp = new HelperClassp(name, email, username, password, policenum, gpsTracker.getLatitude(), gpsTracker.getLongitude());

                    // Push user information to Firebase database
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("police");
                    reference.child(username).setValue(helperClassp);

                    // Inform user
                    Toast.makeText(policesignup.this, "Signup successful", Toast.LENGTH_SHORT).show();

                    // Redirect to login activity
                    Intent intent = new Intent(policesignup.this, policelogin.class);
                    startActivity(intent);
                }
            }
        });
    }
}
