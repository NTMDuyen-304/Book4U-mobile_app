package com.example.book4u.models;

public class HistoryItem {
    private String title;
    private String borrowDate;
    private String returnDate;
    private String status;

    public HistoryItem(String title, String borrowDate, String returnDate, String status) {
        this.title = title;
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public String getBorrowDate() {
        return borrowDate;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public String getStatus() {
        return status;
    }
}