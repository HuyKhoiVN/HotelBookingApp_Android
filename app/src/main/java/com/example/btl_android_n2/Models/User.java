package com.example.btl_android_n2.Models;

public class User {
    private int userId;           // ID người dùng
    private String username;      // Tên đăng nhập
    private String password;      // Mật khẩu
    private String fullName;      // Họ và tên
    private String email;         // Email
    private String phoneNumber;   // Số điện thoại
    private String avatar;        // Hình đại diện
    private String address;       // Địa chỉ
    private String dob;           // Ngày sinh
    private String createdDate;   // Ngày tạo
    private String lastLogin;     // Lần đăng nhập cuối
    private int isActive;         // Trạng thái (1 = hoạt động, 0 = không hoạt động)

    public User(int userId, String username, String password, String fullName, String email, String phoneNumber, String avatar, String address, String dob, String createdDate, String lastLogin, int isActive) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.avatar = avatar;
        this.address = address;
        this.dob = dob;
        this.createdDate = createdDate;
        this.lastLogin = lastLogin;
        this.isActive = isActive;
    }

    public User() {
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(String lastLogin) {
        this.lastLogin = lastLogin;
    }

    public int getIsActive() {
        return isActive;
    }

    public void setIsActive(int isActive) {
        this.isActive = isActive;
    }
}
