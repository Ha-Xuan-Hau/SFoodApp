package com.example.prm392.service;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
public class EmailSender {
    private static final String EMAIL = "quizzesonlinebeta@gmail.com"; // Email gửi OTP
    private static final String PASSWORD = "sstm axqj fnys eawh"; // Mật khẩu ứng dụng
    private static final ExecutorService executor = Executors.newSingleThreadExecutor();

    public static void sendOtp(String recipientEmail, String otpCode) {
        executor.execute(() -> {
            try {
                Properties props = new Properties();
                props.put("mail.smtp.host", "smtp.gmail.com");
                props.put("mail.smtp.socketFactory.port", "465");
                props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.port", "465");

                Session session = Session.getInstance(props, new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(EMAIL, PASSWORD);
                    }
                });

                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(EMAIL));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
                message.setSubject("Your OTP Code");
                message.setText("Your OTP code is: " + otpCode);

                Transport.send(message);
                System.out.println("Email sent successfully");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
