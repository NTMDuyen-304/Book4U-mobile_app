package com.example.book4u.repository;

import com.example.book4u.models.AuthResponse;
import com.example.book4u.models.LoginRequest;
import com.example.book4u.models.User;
import com.example.book4u.network.ApiService;
import com.example.book4u.network.RetrofitClient;
import com.example.book4u.utils.Constants;

import retrofit2.Call;

public class AuthRepository {
    private final ApiService apiService;

    public AuthRepository() {
        apiService = RetrofitClient.getApiService();
    }

    public Call<AuthResponse> login(String email, String password) {
        return apiService.login(new LoginRequest(email, password));
    }

    public Call<User> getProfile(String token) {
        return apiService.getProfile(Constants.BEARER_PREFIX + token);
    }
}