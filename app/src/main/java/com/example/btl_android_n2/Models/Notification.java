package com.example.btl_android_n2.Models;

public class Notification {
    private int notificationId;
    private int userId;
    private String message;
    private String createdDate;
    private int isRead;

    // Constructor
    public Notification(int notificationId, int userId, String message, String createdDate, int isRead) {
        this.notificationId = notificationId;
        this.userId = userId;
        this.message = message;
        this.createdDate = createdDate;
        this.isRead = isRead;
    }

    public int getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(int notificationId) {
        this.notificationId = notificationId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public int getIsRead() {
        return isRead;
    }

    public void setIsRead(int isRead) {
        this.isRead = isRead;
    }
}