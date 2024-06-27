package com.ezen.weather.email;

import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/user")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/send-email")
    @ResponseBody
    public String sendEmail(@RequestBody EmailRequest emailRequest) throws MessagingException {
        emailService.sendSimpleEmail(emailRequest.getEmail());
        return "이메일이 발송되었습니다.";
    }

    @GetMapping("/email")
    public String emailForm() {
        return "email";
    }
}