package com.example.book4u.models;

public class BorrowItem {
    private String title;
    private String borrowDate;
    private String dueDate;
    private String status;

    public BorrowItem(String title, String borrowDate, String dueDate, String status) {
        this.title = title;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
        this.status = status;
    }

    public String getTitle() {
        return title;
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
}