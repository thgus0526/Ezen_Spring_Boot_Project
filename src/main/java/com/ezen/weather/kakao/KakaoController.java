package com.ezen.weather.kakao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;

@Controller
public class KakaoController {

    @Autowired
    private KakaoService kakaoService;

    @RequestMapping("/login")
    public String home(@RequestParam(value = "code", required = false) String code, Model model) throws Exception {
        if (code != null) {
            System.out.println("1----------------여기는 토큰값------------------------1");
            System.out.println("######### Code received: " + code);
            String access_Token = kakaoService.getAccessToken(code);
            System.out.println("### Access Token: " + access_Token);
            System.out.println("2--------------------------------------------------2");

            System.out.println("3-------------------여기는 유저 정보---------------------3");
            HashMap<String, Object> userInfo = kakaoService.getUserInfo(access_Token);
            System.out.println("1.### User Info: " + userInfo);
            System.out.println("2.### Profile Nickname: " + userInfo.get("profile_nickname"));
            System.out.println("3.### Profile Image: " + userInfo.get("profile_image"));
            System.out.println("4.### Email: " + userInfo.get("email"));
            System.out.println("5.### Name: " + userInfo.get("name"));
            System.out.println("6.### Phone Number: " + userInfo.get("phone_number"));
            System.out.println("4--------------------------------------------------4");

            // 사용자 정보를 모델에 추가
            model.addAttribute("userInfo", userInfo);
        } else {
            System.out.println("### 코드 없음요.");
        }

        return "testPage";
    }
}
