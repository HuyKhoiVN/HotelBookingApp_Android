package com.example.btl_android_n2.Models;

public class PaymentHistory {
    private int paymentId;        // ID của giao dịch thanh toán
    private int bookingId;        // ID của lượt đặt phòng liên quan
    private String paymentDate;   // Ngày thanh toán
    private double paymentAmount; // Số tiền thanh toán
    private String paymentMethod; // Phương thức thanh toán

    public PaymentHistory(int paymentId, int bookingId, String paymentDate, double paymentAmount, String paymentMethod) {
        this.paymentId = paymentId;
        this.bookingId = bookingId;
        this.paymentDate = paymentDate;
        this.paymentAmount = paymentAmount;
        this.paymentMethod = paymentMethod;
    }

    public int getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    public double getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(double paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}
