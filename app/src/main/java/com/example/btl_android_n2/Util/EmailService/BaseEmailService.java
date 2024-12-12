package com.example.btl_android_n2.Util.EmailService;

import android.os.AsyncTask;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

public abstract class BaseEmailService {

    private final String smtpHost = "smtp.gmail.com";
    private final int smtpPort = 587;
    private final String emailUsername = "huykhoibtldidong@gmail.com\n"; // Thay bằng email của bạn
    private final String emailPassword = "Huykhoi210";       // Thay bằng mật khẩu ứng dụng

    // Các phương thức trừu tượng sẽ được ghi đè trong lớp con
    protected abstract String getSubject();  // Chủ đề email
    protected abstract String getBody();     // Nội dung email
    protected abstract String getRecipient(); // Địa chỉ email người nhận

    // Phương thức gửi email
    public void sendEmail() {
        new SendEmailTask().execute();
    }

    // Lớp AsyncTask để thực hiện việc gửi email trong nền
    private class SendEmailTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", smtpHost);
            props.put("mail.smtp.port", smtpPort);

            // Tạo session với thông tin đăng nhập
            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(emailUsername, emailPassword);
                }
            });

            try {
                // Tạo và thiết lập thông tin email
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(emailUsername));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(getRecipient()));
                message.setSubject(getSubject());
                message.setText(getBody());

                // Gửi email
                Transport.send(message);
                System.out.println("Email sent successfully to " + getRecipient());
            } catch (MessagingException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
