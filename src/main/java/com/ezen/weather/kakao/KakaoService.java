package com.ezen.weather.kakao;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

@Service
public class KakaoService {

    @Value("${kakao.client-id}")
    private String kakao_client_id;

    @Autowired
    private KakaoUserInfoRepository kakaoUserInfoRepository;

    public String getAccessToken(String code) {
        String accessToken = "";
        String reqURL = "https://kauth.kakao.com/oauth/token";

        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            String postParams = "grant_type=authorization_code"
                    + "&client_id=" + kakao_client_id
                    + "&redirect_uri=http://localhost:8080/login"
                    + "&code=" + code;
            conn.getOutputStream().write(postParams.getBytes());

            int responseCode = conn.getResponseCode();
            System.out.println("responseCode : " + responseCode);

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            StringBuilder result = new StringBuilder();
            while ((line = br.readLine()) != null) {
                result.append(line);
            }
            System.out.println("response body : " + result);

            JsonElement element = JsonParser.parseString(result.toString());
            accessToken = element.getAsJsonObject().get("access_token").getAsString();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return accessToken;
    }

    public HashMap<String, Object> getUserInfo(String access_Token) {
        HashMap<String, Object> userInfo = new HashMap<>();
        String reqURL = "https://kapi.kakao.com/v2/user/me";
        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            conn.setRequestProperty("Authorization", "Bearer " + access_Token);

            int responseCode = conn.getResponseCode();
            System.out.println("responseCode : " + responseCode);

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String line;
            StringBuilder result = new StringBuilder();

            while ((line = br.readLine()) != null) {
                result.append(line);
            }
            System.out.println("response body : " + result);

            JsonElement element = JsonParser.parseString(result.toString());

            JsonObject properties = element.getAsJsonObject().get("properties").getAsJsonObject();
            String nickname = properties.get("nickname").getAsString();
            String profile_image = properties.get("profile_image").getAsString();

            JsonObject kakaoAccount = element.getAsJsonObject().get("kakao_account").getAsJsonObject();
            String email = kakaoAccount.get("email").getAsString();
            String name = kakaoAccount.get("name").getAsString();
            String phoneNumber = kakaoAccount.get("phone_number").getAsString();

            userInfo.put("profile_nickname", nickname);
            userInfo.put("profile_image", profile_image);
            userInfo.put("email", email);
            userInfo.put("name", name);
            userInfo.put("phone_number", phoneNumber);

            // 사용자 정보를 데이터베이스에 저장
            KakaoUserInfo user = new KakaoUserInfo();
            user.setNickname(nickname);
            user.setProfileImage(profile_image);
            user.setEmail(email);
            user.setName(name);
            user.setPhoneNumber(phoneNumber);

            kakaoUserInfoRepository.save(user);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return userInfo;
    }
}
