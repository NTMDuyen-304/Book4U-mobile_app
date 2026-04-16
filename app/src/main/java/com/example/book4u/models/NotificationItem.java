package com.example.book4u.models;

public class NotificationItem {
    private String id;
    private String userId;
    private String title;
    private String message;
    private String createdAt;
    private boolean isRead;

    public NotificationItem(String id, String userId, String title, String message, String createdAt, boolean isRead) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.message = message;
        this.createdAt = createdAt;
        this.isRead = isRead;
    }

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }
}