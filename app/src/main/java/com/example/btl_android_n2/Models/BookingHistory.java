package com.example.btl_android_n2.Models;

public class BookingHistory {
    private int historyId;        // ID của lịch sử đặt phòng
    private int userId;           // ID của người dùng
    private int roomId;           // ID của phòng
    private String userAction;    // Hành động của người dùng (đặt, hủy,...)
    private String actionDate;    // Ngày thực hiện hành động

    public BookingHistory(int historyId, int userId, int roomId, String userAction, String actionDate) {
        this.historyId = historyId;
        this.userId = userId;
        this.roomId = roomId;
        this.userAction = userAction;
        this.actionDate = actionDate;
    }

    public int getHistoryId() {
        return historyId;
    }

    public void setHistoryId(int historyId) {
        this.historyId = historyId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public String getUserAction() {
        return userAction;
    }

    public void setUserAction(String userAction) {
        this.userAction = userAction;
    }

    public String getActionDate() {
        return actionDate;
    }

    public void setActionDate(String actionDate) {
        this.actionDate = actionDate;
    }
}
