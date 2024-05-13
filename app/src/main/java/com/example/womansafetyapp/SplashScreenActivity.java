package com.example.womansafetyapp;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class SplashScreenActivity extends AppCompatActivity {


    ProgressBar progressBar;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_welcome);
//        bundle = getIntent().getExtras();
//
//
//        progressBar = (ProgressBar) findViewById(R.id.progressBar2);
//        progressBar.setMax(100);
//        progressBar.setProgress(0);
//        progressBar.getProgressDrawable().setColorFilter(
//                Color.WHITE, android.graphics.PorterDuff.Mode.SRC_IN);
//
//        Thread thread = new Thread() {
//
//            public void run() {
//
//                try {
//                    for (int i = 0; i < 100; i++) {
//                        progressBar.setProgress(i);
//                        sleep(20);
//                    }
//
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                } finally {
//
//                    startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
//                    finish();
//
//
//                }
//            }
//        };
//        thread.start();


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // on below line we are
                // creating a new intent
                Intent i = new Intent(SplashScreenActivity.this, MainActivity.class);

                // on below line we are
                // starting a new activity.
                startActivity(i);

                // on the below line we are finishing
                // our current activity.
                finish();
            }
        }, 2000);



    }


}
