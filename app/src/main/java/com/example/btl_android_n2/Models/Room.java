package com.example.btl_android_n2.Models;

public class Room {
    private int roomId;
    private String roomName;
    private String roomType;
    private int peopleNumber;
    private double price;
    private String description;
    private double review;
    private String status;
    private int provinceId;
    private String image;
    private String address;

    public Room(int roomId, String roomName, String roomType, int peopleNumber, double price, String description, double review, String status, int provinceId, String image, String address) {
        this.roomId = roomId;
        this.roomName = roomName;
        this.roomType = roomType;
        this.peopleNumber = peopleNumber;
        this.price = price;
        this.description = description;
        this.review = review;
        this.status = status;
        this.provinceId = provinceId;
        this.image = image;
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public int getPeopleNumber() {
        return peopleNumber;
    }

    public void setPeopleNumber(int peopleNumber) {
        this.peopleNumber = peopleNumber;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getReview() {
        return review;
    }

    public void setReview(double review) {
        this.review = review;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
