package com.example.book4u.network;

import com.example.book4u.models.AuthResponse;
import com.example.book4u.models.Book;
import com.example.book4u.models.BookRequest;
import com.example.book4u.models.Borrow;
import com.example.book4u.models.BorrowCreateRequest;
import com.example.book4u.models.LoginRequest;
import com.example.book4u.models.MessageResponse;
import com.example.book4u.models.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    // AUTH
    @POST("api/auth/login")
    Call<AuthResponse> login(@Body LoginRequest request);

    @GET("api/auth/profile")
    Call<User> getProfile(@Header("Authorization") String bearerToken);

    // BOOKS
    @GET("api/books")
    Call<List<Book>> getBooks(
            @Header("Authorization") String bearerToken,
            @Query("keyword") String keyword,
            @Query("category") String category
    );

    @GET("api/books/{id}")
    Call<Book> getBookById(
            @Header("Authorization") String bearerToken,
            @Path("id") String id
    );

    @POST("api/books")
    Call<Book> createBook(
            @Header("Authorization") String bearerToken,
            @Body BookRequest request
    );

    @PUT("api/books/{id}")
    Call<Book> updateBook(
            @Header("Authorization") String bearerToken,
            @Path("id") String id,
            @Body BookRequest request
    );

    @DELETE("api/books/{id}")
    Call<MessageResponse> deleteBook(
            @Header("Authorization") String bearerToken,
            @Path("id") String id
    );

    // BORROW
    @GET("api/borrow")
    Call<List<Borrow>> getAllBorrows(
            @Header("Authorization") String bearerToken
    );

    @GET("api/borrow/me")
    Call<List<Borrow>> getMyBorrows(
            @Header("Authorization") String bearerToken
    );

    @GET("api/borrow/user/{userId}")
    Call<List<Borrow>> getBorrowsByUser(
            @Header("Authorization") String bearerToken,
            @Path("userId") String userId
    );

    @POST("api/borrow")
    Call<Borrow> createBorrow(
            @Header("Authorization") String bearerToken,
            @Body BorrowCreateRequest request
    );

    @PUT("api/borrow/{id}/approve")
    Call<Borrow> approveBorrow(
            @Header("Authorization") String bearerToken,
            @Path("id") String borrowId
    );

    @PUT("api/borrow/{id}/reject")
    Call<Borrow> rejectBorrow(
            @Header("Authorization") String bearerToken,
            @Path("id") String borrowId
    );

    @PUT("api/borrow/{id}/extend")
    Call<Borrow> extendBorrow(
            @Header("Authorization") String bearerToken,
            @Path("id") String borrowId
    );

    @PUT("api/borrow/{id}/return")
    Call<Borrow> returnBorrow(
            @Header("Authorization") String bearerToken,
            @Path("id") String borrowId
    );

    @DELETE("api/borrow/{id}")
    Call<MessageResponse> deleteBorrow(
            @Header("Authorization") String bearerToken,
            @Path("id") String borrowId
    );
}