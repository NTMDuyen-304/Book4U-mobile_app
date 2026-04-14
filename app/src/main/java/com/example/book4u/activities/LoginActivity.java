package com.example.book4u.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.book4u.R;
import com.example.book4u.storage.SessionManager;

public class LoginActivity extends AppCompatActivity {

    private EditText edtEmail, edtPassword;
    private Button btnLogin;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);

        sessionManager = new SessionManager(this);

        btnLogin.setOnClickListener(v -> doLogin());
    }

    private void doLogin() {
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Vui lòng nhập đủ tài khoản và mật khẩu", Toast.LENGTH_SHORT).show();
            return;
        }

        String role = email.toLowerCase().contains("admin") ? "admin" : "student";

        sessionManager.saveSession(
                "mock_token_123",
                "u001",
                role.equals("admin") ? "Admin User" : "Student User",
                email,
                role
        );

        Toast.makeText(this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();

        if ("admin".equalsIgnoreCase(role)) {
            startActivity(new Intent(LoginActivity.this, AdminMainActivity.class));
        } else {
            startActivity(new Intent(LoginActivity.this, StudentMainActivity.class));
        }

        finish();
    }
}