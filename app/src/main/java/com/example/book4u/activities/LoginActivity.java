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
import com.example.book4u.models.User;
import com.example.book4u.repository.AuthRepository;
import com.example.book4u.storage.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText edtEmail, edtPassword;
    private Button btnLogin;

    private SessionManager sessionManager;
    private AuthRepository authRepository;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);

        sessionManager = new SessionManager(this);
        authRepository = new AuthRepository();

        btnLogin.setOnClickListener(v -> doLogin());
    }

    private void doLogin() {
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Vui lòng nhập đủ tài khoản và mật khẩu", Toast.LENGTH_SHORT).show();
            return;
        }

        setLoading(true);

        authRepository.login(email, password).enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                setLoading(false);

                if (response.isSuccessful() && response.body() != null) {
                    AuthResponse authResponse = response.body();
                    User user = authResponse.getUser();

                    if (authResponse.getToken() == null || user == null) {
                        Toast.makeText(LoginActivity.this, "Dữ liệu đăng nhập không hợp lệ", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    sessionManager.saveSession(
                            authResponse.getToken(),
                            user.getId(),
                            user.getName(),
                            user.getEmail(),
                            user.getRole()
                    );

                    Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();

                    if ("admin".equalsIgnoreCase(user.getRole())) {
                        startActivity(new Intent(LoginActivity.this, AdminMainActivity.class));
                    } else {
                        startActivity(new Intent(LoginActivity.this, StudentMainActivity.class));
                    }

                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Sai tài khoản hoặc mật khẩu", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                setLoading(false);
                Toast.makeText(LoginActivity.this, "Lỗi kết nối BE: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setLoading(boolean isLoading) {
        btnLogin.setEnabled(!isLoading);
        btnLogin.setText(isLoading ? "Đang đăng nhập..." : "Đăng nhập");
    }
}