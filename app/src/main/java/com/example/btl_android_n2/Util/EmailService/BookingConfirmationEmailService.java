package com.example.btl_android_n2.Util.EmailService;

public class BookingConfirmationEmailService extends BaseEmailService {
    private final String recipientEmail;
    private final String fullName;
    private final String roomName;
    private final String checkInDate;
    private final String checkOutDate;
    private final double totalPrice;

    // Constructor nhận các tham số cần thiết cho email
    public BookingConfirmationEmailService(String recipientEmail, String fullName, String roomName,
                                           String checkInDate, String checkOutDate, double totalPrice) {
        this.recipientEmail = recipientEmail;
        this.fullName = fullName;
        this.roomName = roomName;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.totalPrice = totalPrice;
    }

    @Override
    protected String getSubject() {
        // Chủ đề email
        return "Xác nhận đặt phòng thành công!";
    }

    @Override
    protected String getBody() {
        // Nội dung email
        return "Xin chào " + fullName + ",\n\n"
                + "Cảm ơn bạn đã đặt phòng tại hệ thống của chúng tôi. Dưới đây là thông tin chi tiết:\n\n"
                + "Tên phòng: " + roomName + "\n"
                + "Ngày nhận phòng: " + checkInDate + "\n"
                + "Ngày trả phòng: " + checkOutDate + "\n"
                + "Tổng tiền: " + String.format("%,.2f", totalPrice) + " VND\n\n"
                + "Chúng tôi rất mong được chào đón bạn!\n\n"
                + "Trân trọng,\nĐội ngũ hỗ trợ";
    }

    @Override
    protected String getRecipient() {
        // Địa chỉ email người nhận
        return recipientEmail;
    }
}
