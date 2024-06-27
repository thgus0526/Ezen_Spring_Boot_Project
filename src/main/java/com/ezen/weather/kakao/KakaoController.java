package com.ezen.weather.kakao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class KakaoController {

    private static final Logger logger = LoggerFactory.getLogger(KakaoController.class);

    @Autowired
    private KakaoService kakaoService;



    @RequestMapping("/login")
    public String home(@RequestParam(value = "code", required = false) String code, Model model) {
        System.out.println("시작");
        try {
            if (code != null) {
                logger.info("----------------여기는 토큰값------------------------");
                logger.info("######### Code received: {}", code);
                String access_Token = kakaoService.getAccessToken(code);
                logger.info("### Access Token: {}", access_Token);
                logger.info("----------------여기는 토큰값------------------------");

                logger.info("-------------------여기는 유저 정보---------------------");
                HashMap<String, Object> userInfo = kakaoService.getUserInfo(access_Token);
                logger.info("1.### User Info: {}", userInfo);
                logger.info("2.### Profile Nickname: {}", userInfo.get("profile_nickname"));
                logger.info("3.### Profile Image: {}", userInfo.get("profile_image"));
                logger.info("4.### Email: {}", userInfo.get("email"));
                logger.info("5.### Name: {}", userInfo.get("name"));
                logger.info("6.### Phone Number: {}", userInfo.get("phone_number"));
                logger.info("-------------------여기는 유저 정보---------------------");

                // 사용자 정보를 모델에 추가
                model.addAttribute("access_Token", access_Token);
                model.addAttribute("userInfo", userInfo);

            } else {
                logger.warn("### 코드 없음요.");
            }
        } catch (Exception e) {
            logger.error("로그인 처리 중 오류 발생", e);
            // 에러 페이지로 리다이렉트하거나 에러 메시지 제공
            return "redirect:/error";
        }
        return "kakaoindex";
    }
}
