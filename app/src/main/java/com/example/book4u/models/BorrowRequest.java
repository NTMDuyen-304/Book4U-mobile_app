package com.example.book4u.models;

public class BorrowRequest {
    private String id;
    private String bookTitle;
    private String userId;
    private String userName;
    private String requestDate;
    private String borrowDate;
    private String dueDate;
    private String status;

    public BorrowRequest(String id, String bookTitle, String userId, String userName,
                         String requestDate, String borrowDate, String dueDate, String status) {
        this.id = id;
        this.bookTitle = bookTitle;
        this.userId = userId;
        this.userName = userName;
        this.requestDate = requestDate;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getRequestDate() {
        return requestDate;
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

    public void setBorrowDate(String borrowDate) {
        this.borrowDate = borrowDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}