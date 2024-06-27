package com.ezen.weather.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendSimpleEmail(String to) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true); // true indicates HTML format

        String subject = "Weather Ezen Spring Boot 이메일 인증입니다.";

        final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz01234567890123456789";
        final int LENGTH = 6;
        final Random random = new Random();

        StringBuilder sb = new StringBuilder();
        sb.append("<html><body>");
        sb.append("<h2 style='color: #007bff;'>Weather Ezen Spring Boot 이메일 인증 코드</h2>");
        sb.append("<p>아래 코드를 입력하여 이메일 인증을 완료해주세요:</p>");
        sb.append("<p style='font-size: 18px; font-weight: bold;'>");

        for (int i = 0; i < LENGTH; i++) {
            int index = random.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(index));
        }

        sb.append("</p>");
        sb.append("</body></html>");

        String authenticationCode = sb.toString();

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(authenticationCode, true); // HTML 형식으로 설정

        mailSender.send(message);
    }
}