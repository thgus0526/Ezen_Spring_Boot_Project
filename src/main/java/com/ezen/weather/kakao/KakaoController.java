package com.ezen.weather.kakao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.servlet.http.HttpSession;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class KakaoController {

    private static final Logger logger = LoggerFactory.getLogger(KakaoController.class);

    private final KakaoService kakaoService;

    @Autowired
    public KakaoController(KakaoService kakaoService) {
        this.kakaoService = kakaoService;
    }


    @RequestMapping("/login")
    public String home(@RequestParam(value = "code", required = false) String code, Model model, HttpSession session) {
        logger.info("카카오 로그인 시작");

        try {
            if (code != null) {
                logger.info("인증 코드 수신: {}", code);
                String access_Token = kakaoService.getAccessToken(code);
                logger.info("액세스 토큰: {}", access_Token);

                HashMap<String, Object> userInfo = kakaoService.getUserInfo(access_Token);
                logger.info("유저 정보 수신: {}", userInfo);

                model.addAttribute("access_Token", access_Token);
                model.addAttribute("userInfo", userInfo);

                // 로그인 성공 메시지를 세션에 저장
                session.setAttribute("loginMessage", "카카오톡 로그인이 완료되었습니다.");

                // 원하는 페이지로 리다이렉트
                return "redirect:/user/signup";
            } else {
                logger.warn("인증 코드가 전달되지 않았습니다.");
            }
        } catch (Exception e) {
            logger.error("로그인 처리 중 오류 발생", e);
            model.addAttribute("errorMessage", "로그인 처리 중 오류가 발생했습니다. 다시 시도해 주세요.");
            return "error";
        }
        return "index";
    }

    @GetMapping("/logout")
    public String yourPage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();  // 사용자의 이메일을 가져옵니다.
        model.addAttribute("userEmail", email);
        return "index";
    }

}