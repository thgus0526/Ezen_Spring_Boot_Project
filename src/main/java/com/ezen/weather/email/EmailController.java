package com.ezen.weather.email;

import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Controller
@RequestMapping("/user")
public class EmailController {

    @Autowired
    private EmailService emailService;

    private Map<String, String> verificationCodes = new ConcurrentHashMap<>();

    @PostMapping("/send-email")
    @ResponseBody
    public String sendEmail(@RequestBody EmailRequest emailRequest) throws MessagingException {
        String email = emailRequest.getEmail();
        String verificationCode = generateVerificationCode();

        // 인증 코드를 메모리에 저장
        verificationCodes.put(email, verificationCode);

        emailService.sendSimpleEmail(email, verificationCode);
        return "이메일이 발송되었습니다.";
    }

    @PostMapping("/verify-code")
    @ResponseBody
    public Map<String, Object> verifyCode(@RequestBody VerificationRequest verificationRequest) {
        String email = verificationRequest.getEmail();
        String code = verificationRequest.getCode();
        Map<String, Object> response = new HashMap<>();

        // 메모리에서 인증 코드 조회
        String savedCode = verificationCodes.get(email);

        if (savedCode != null && savedCode.equals(code)) {
            response.put("success", true);
            response.put("message", "인증 코드가 확인되었습니다.");
            // 인증 성공 시 추가 작업 수행
            // 예: 회원 가입 버튼 활성화, 다음 단계로 진행 등

            // 인증 코드 사용 후 메모리에서 삭제
            verificationCodes.remove(email);
        } else {
            response.put("success", false);
            response.put("message", "잘못된 인증 코드입니다.");
        }
        return response;
    }

    private String generateVerificationCode() {
        final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz01234567890123456789";
        final int LENGTH = 6;
        final Random random = new Random();

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < LENGTH; i++) {
            int index = random.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(index));
        }

        return sb.toString();
    }
}