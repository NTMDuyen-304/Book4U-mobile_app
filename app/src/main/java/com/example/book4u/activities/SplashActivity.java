package com.example.book4u.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.book4u.R;
import com.example.book4u.storage.SessionManager;

public class SplashActivity extends AppCompatActivity {

    private SessionManager sessionManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        sessionManager = new SessionManager(this);

        if (sessionManager.isLoggedIn()) {
            if ("admin".equalsIgnoreCase(sessionManager.getRole())) {
                startActivity(new Intent(this, AdminMainActivity.class));
            } else {
                startActivity(new Intent(this, StudentMainActivity.class));
            }
        } else {
            startActivity(new Intent(this, LoginActivity.class));
        }

        finish();
    }
}