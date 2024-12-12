package com.example.btl_android_n2.Models;

public class FavoriteRoom {
    private int favoriteId;
    private int userId;
    private int roomId;
    private String createdDate;

    // Constructor
    public FavoriteRoom(int favoriteId, int userId, int roomId, String createdDate) {
        this.favoriteId = favoriteId;
        this.userId = userId;
        this.roomId = roomId;
        this.createdDate = createdDate;
    }

    // Getter and Setter methods
    public int getFavoriteId() {
        return favoriteId;
    }

    public void setFavoriteId(int favoriteId) {
        this.favoriteId = favoriteId;
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

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }
}
