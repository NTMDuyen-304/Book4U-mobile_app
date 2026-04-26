package com.example.book4u.models;

public class BorrowRequestBody {

    private String bookId;

    public BorrowRequestBody(String bookId) {
        this.bookId = bookId;
    }

    public String getBookId() {
        return bookId;
    }
}