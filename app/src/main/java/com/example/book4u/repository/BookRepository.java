package com.example.book4u.repository;

import com.example.book4u.models.Book;
import com.example.book4u.models.BookRequest;
import com.example.book4u.models.MessageResponse;
import com.example.book4u.network.ApiService;
import com.example.book4u.network.RetrofitClient;
import com.example.book4u.utils.Constants;

import java.util.List;

import retrofit2.Call;

public class BookRepository {
    private final ApiService apiService;

    public BookRepository() {
        apiService = RetrofitClient.getApiService();
    }

    private String bearer(String token) {
        return Constants.BEARER_PREFIX + token;
    }

    public Call<List<Book>> getBooks(String token, String keyword, String category) {
        return apiService.getBooks(bearer(token), keyword, category);
    }

    public Call<Book> getBookById(String token, String bookId) {
        return apiService.getBookById(bearer(token), bookId);
    }

    public Call<Book> createBook(String token, BookRequest request) {
        return apiService.createBook(bearer(token), request);
    }

    public Call<Book> updateBook(String token, String bookId, BookRequest request) {
        return apiService.updateBook(bearer(token), bookId, request);
    }

    public Call<MessageResponse> deleteBook(String token, String bookId) {
        return apiService.deleteBook(bearer(token), bookId);
    }
}