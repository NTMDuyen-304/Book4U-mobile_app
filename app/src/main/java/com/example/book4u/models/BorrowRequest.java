package com.example.book4u.models;

public class BorrowRequest {
    private String bookTitle;
    private String userName;
    private String requestDate;
    private String status;

    public BorrowRequest(String bookTitle, String userName, String requestDate, String status) {
        this.bookTitle = bookTitle;
        this.userName = userName;
        this.requestDate = requestDate;
        this.status = status;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public String getUserName() {
        return userName;
    }

    public String getRequestDate() {
        return requestDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}