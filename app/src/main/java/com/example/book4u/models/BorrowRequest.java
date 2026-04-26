package com.example.book4u.models;

public class BorrowRequest {
    private String id;
    private String bookTitle;
    private String userId;
    private String userName;
    private String userEmail;
    private String requestDate;
    private String borrowDate;
    private String dueDate;
    private String status;

    public BorrowRequest(String id,
                         String bookTitle,
                         String userId,
                         String userName,
                         String userEmail,
                         String requestDate,
                         String borrowDate,
                         String dueDate,
                         String status) {
        this.id = id;
        this.bookTitle = bookTitle;
        this.userId = userId;
        this.userName = userName;
        this.userEmail = userEmail;
        this.requestDate = requestDate;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
        this.status = status;
    }

    public static BorrowRequest fromBorrow(Borrow borrow) {
        return new BorrowRequest(
                borrow.getId(),
                borrow.getBookTitle(),
                borrow.getUserId(),
                borrow.getBorrowerName(),
                borrow.getBorrowerEmail(),
                borrow.getBorrowDate(),
                borrow.getBorrowDate(),
                borrow.getDueDate(),
                borrow.getStatus()
        );
    }

    public String getId() {
        return id != null ? id : "";
    }

    public String getBookTitle() {
        return bookTitle != null && !bookTitle.isEmpty() ? bookTitle : "Unknown Book";
    }

    public String getUserId() {
        return userId != null ? userId : "";
    }

    public String getUserName() {
        return userName != null && !userName.isEmpty() ? userName : "Unknown User";
    }

    public String getUserEmail() {
        return userEmail != null ? userEmail : "";
    }

    public String getRequestDate() {
        return requestDate != null && !requestDate.isEmpty() ? requestDate : "-";
    }

    public String getBorrowDate() {
        return borrowDate != null && !borrowDate.isEmpty() ? borrowDate : "-";
    }

    public String getDueDate() {
        return dueDate != null && !dueDate.isEmpty() ? dueDate : "-";
    }

    public String getStatus() {
        return status != null && !status.isEmpty() ? status : "Pending Approval";
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