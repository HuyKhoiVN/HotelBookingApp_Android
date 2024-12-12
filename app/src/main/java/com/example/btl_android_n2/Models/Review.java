package com.example.btl_android_n2.Models;

public class Review {
    private int reviewId;         // ID đánh giá
    private int userId;           // ID người dùng
    private int roomId;           // ID phòng
    private double rating;        // Xếp hạng (giá trị từ 0 đến 5)
    private String comment;       // Nội dung đánh giá
    private String createdDate;   // Ngày tạo đánh giá

    public Review(int reviewId, int userId, int roomId, double rating, String comment, String createdDate) {
        this.reviewId = reviewId;
        this.userId = userId;
        this.roomId = roomId;
        this.rating = rating;
        this.comment = comment;
        this.createdDate = createdDate;
    }

    public Review() {
    }

    public int getReviewId() {
        return reviewId;
    }

    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
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

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }
}
