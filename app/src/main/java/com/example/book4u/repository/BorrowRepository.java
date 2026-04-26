package com.example.book4u.repository;

import com.example.book4u.models.Borrow;
import com.example.book4u.models.BorrowCreateRequest;
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

    public Call<Borrow> createBorrow(String token, String bookId) {
        return apiService.createBorrow(
                Constants.BEARER_PREFIX + token,
                new BorrowCreateRequest(bookId)        );
    }

    public Call<List<Borrow>> getMyBorrows(String token) {
        return apiService.getMyBorrows(Constants.BEARER_PREFIX + token);
    }

    public Call<List<Borrow>> getAllBorrows(String token) {
        return apiService.getAllBorrows(Constants.BEARER_PREFIX + token);
    }

    public Call<Borrow> approveBorrow(String token, String borrowId) {
        return apiService.approveBorrow(Constants.BEARER_PREFIX + token, borrowId);
    }

    public Call<Borrow> rejectBorrow(String token, String borrowId) {
        return apiService.rejectBorrow(Constants.BEARER_PREFIX + token, borrowId);
    }

    public Call<Borrow> returnBorrow(String token, String borrowId) {
        return apiService.returnBorrow(Constants.BEARER_PREFIX + token, borrowId);
    }
}