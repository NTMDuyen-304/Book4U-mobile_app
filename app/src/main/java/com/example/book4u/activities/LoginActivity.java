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
import com.example.book4u.models.AuthResponse;
import com.example.book4u.repository.AuthRepository;
import com.example.book4u.storage.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText edtEmail, edtPassword;
    private Button btnLogin;

    private AuthRepository authRepository;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);

        authRepository = new AuthRepository();
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

        authRepository.login(email, password).enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (!response.isSuccessful() || response.body() == null || response.body().getUser() == null) {
                    Toast.makeText(LoginActivity.this, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
                    return;
                }

                AuthResponse auth = response.body();

                sessionManager.saveSession(
                        auth.getToken(),
                        auth.getUser().getId(),
                        auth.getUser().getName(),
                        auth.getUser().getEmail(),
                        auth.getUser().getRole()
                );

                if ("admin".equalsIgnoreCase(auth.getUser().getRole())) {
                    startActivity(new Intent(LoginActivity.this, AdminMainActivity.class));
                } else {
                    startActivity(new Intent(LoginActivity.this, StudentMainActivity.class));
                }

                finish();
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}