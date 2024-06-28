package com.ezen.weather.kakao;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class KakaoUserInfo {

    @Id
    private String email;
    private String nickname;
    private String profileImage;
    private String name;
    private String phoneNumber;

    // 기본 생성자
    public KakaoUserInfo() {}

    // 매개변수가 있는 생성자
    public KakaoUserInfo(String email, String nickname, String profileImage, String name, String phoneNumber) {
        this.email = email;
        this.nickname = nickname;
        this.profileImage = profileImage;
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    // Getter와 Setter 메서드
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
