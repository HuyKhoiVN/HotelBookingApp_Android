package com.example.btl_android_n2.Util.EmailService;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

public class EmailSender {

    // Hàm gửi email
    public static boolean sendEmail(String toEmail, String subject, String body) {
        // Thông tin tài khoản Gmail của bạn
        final String fromEmail = "huykhoibtldidong@gmail.com";  // Thay đổi thành email của bạn
        final String password = "Huykhoi210";     // Thay đổi mật khẩu ứng dụng hoặc mật khẩu Gmail của bạn

        // Cấu hình server của Gmail
        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "465"); // Sử dụng cổng SSL
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true"); // Kích hoạt STARTTLS

        // Tạo phiên làm việc (session)
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password); // Sử dụng email và mật khẩu của bạn
            }
        });

        try {
            // Tạo đối tượng MimeMessage
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail)); // Địa chỉ email gửi đi
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmail)); // Địa chỉ email nhận
            message.setSubject(subject); // Tiêu đề email
            message.setText(body); // Nội dung email (mã OTP)

            // Gửi email
            Transport.send(message);
            return true; // Gửi email thành công
        } catch (MessagingException e) {
            e.printStackTrace();
            return false; // Gửi email thất bại
        }
    }

    // Hàm tạo mã OTP ngẫu nhiên
    public static String generateOTP() {
        Random random = new Random();
        String otp = String.format("%06d", random.nextInt(999999)); // Mã OTP 6 chữ số
        return otp;
    }
}
