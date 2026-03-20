package com.example.book4u.repository;

import com.example.book4u.models.Borrow;
import com.example.book4u.models.BorrowCreateRequest;
import com.example.book4u.models.MessageResponse;
import com.example.book4u.network.ApiService;
import com.example.book4u.network.RetrofitClient;
import com.example.book4u.utils.Constants;

import java.util.List;

import retrofit2.Call;

public class BorrowRepository {
    private final ApiService apiService;

    public BorrowRepository() {
        apiService = RetrofitClient.getApiService();
    }

    private String bearer(String token) {
        return Constants.BEARER_PREFIX + token;
    }

    public Call<List<Borrow>> getAllBorrows(String token) {
        return apiService.getAllBorrows(bearer(token));
    }

    public Call<List<Borrow>> getMyBorrows(String token) {
        return apiService.getMyBorrows(bearer(token));
    }

    public Call<List<Borrow>> getBorrowsByUser(String token, String userId) {
        return apiService.getBorrowsByUser(bearer(token), userId);
    }

    public Call<Borrow> createBorrow(String token, String bookId) {
        return apiService.createBorrow(bearer(token), new BorrowCreateRequest(bookId));
    }

    public Call<Borrow> approveBorrow(String token, String borrowId) {
        return apiService.approveBorrow(bearer(token), borrowId);
    }

    public Call<Borrow> rejectBorrow(String token, String borrowId) {
        return apiService.rejectBorrow(bearer(token), borrowId);
    }

    public Call<Borrow> extendBorrow(String token, String borrowId) {
        return apiService.extendBorrow(bearer(token), borrowId);
    }

    public Call<Borrow> returnBorrow(String token, String borrowId) {
        return apiService.returnBorrow(bearer(token), borrowId);
    }

    public Call<MessageResponse> deleteBorrow(String token, String borrowId) {
        return apiService.deleteBorrow(bearer(token), borrowId);
    }
}