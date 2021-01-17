package com.ahmadabuhasan.skripsi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.android.gms.auth.api.credentials.CredentialsApi;

/*
 * Created by Ahmad Abu Hasan on 17/01/2021
 */

public class SplashScreen extends AppCompatActivity {

    public static int splashTimeOut = CredentialsApi.CREDENTIAL_PICKER_REQUEST_CODE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        getSupportActionBar().hide();

        new Handler().postDelayed(new Runnable() {
            public void run() {
                SplashScreen.this.startActivity(new Intent(SplashScreen.this, DashboardActivity.class));
                SplashScreen.this.finish();
            }
        }, (long) splashTimeOut);
    }
}