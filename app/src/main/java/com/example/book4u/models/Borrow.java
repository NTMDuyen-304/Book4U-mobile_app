package com.example.book4u.models;

import com.google.gson.annotations.SerializedName;

public class Borrow {
    @SerializedName(value = "id", alternate = {"_id"})
    private String id;

    private BorrowUserLite user;
    private BorrowBookLite book;
    private String borrower;
    private String borrowDate;
    private String dueDate;
    private String status;
    private int extendedDays;
    private String createdAt;
    private String updatedAt;

    public String getId() {
        return id;
    }

    public BorrowUserLite getUser() {
        return user;
    }

    public BorrowBookLite getBook() {
        return book;
    }

    public String getBorrower() {
        return borrower;
    }

    public String getBorrowDate() {
        return borrowDate;
    }

    public String getDueDate() {
        return dueDate;
    }

    public String getStatus() {
        return status;
    }

    public int getExtendedDays() {
        return extendedDays;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }
}