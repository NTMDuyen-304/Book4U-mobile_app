package com.example.book4u.models;

import com.google.gson.annotations.SerializedName;

public class Borrow {

    @SerializedName(value = "id", alternate = {"_id"})
    private String id;

    private String bookId;
    private String userId;

    private String title;
    private String bookTitle;

    private String borrowerName;
    private String borrowerEmail;

    private String borrowDate;
    private String dueDate;
    private String status;

    private Integer daysRemaining;
    private int extendedDays;

    public String getId() {
        return id;
    }

    public String getBookId() {
        return bookId != null ? bookId : "";
    }

    public String getUserId() {
        return userId != null ? userId : "";
    }

    public String getTitle() {
        if (title != null && !title.isEmpty()) {
            return title;
        }

        if (bookTitle != null && !bookTitle.isEmpty()) {
            return bookTitle;
        }

        return "Unknown Book";
    }

    public String getBookTitle() {
        return getTitle();
    }

    public String getBorrowerName() {
        return borrowerName != null ? borrowerName : "Unknown";
    }

    public String getBorrowerEmail() {
        return borrowerEmail != null ? borrowerEmail : "";
    }

    public String getBorrowDate() {
        return borrowDate != null && !borrowDate.isEmpty() ? borrowDate : "-";
    }

    public String getDueDate() {
        return dueDate != null && !dueDate.isEmpty() ? dueDate : "-";
    }

    public String getStatus() {
        return status != null ? status : "Unknown";
    }

    public Integer getDaysRemaining() {
        return daysRemaining;
    }

    public int getExtendedDays() {
        return extendedDays;
    }

    public boolean isPending() {
        return "Pending Approval".equalsIgnoreCase(getStatus())
                || "Pending".equalsIgnoreCase(getStatus());
    }

    public boolean isBorrowing() {
        return "Borrowing".equalsIgnoreCase(getStatus())
                || "Overdue".equalsIgnoreCase(getStatus());
    }

    public boolean isReturned() {
        return "Returned".equalsIgnoreCase(getStatus());
    }

    public boolean isRejected() {
        return "Rejected".equalsIgnoreCase(getStatus());
    }
}