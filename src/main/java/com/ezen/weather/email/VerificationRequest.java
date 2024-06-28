package com.ezen.weather.email;

public class VerificationRequest {

    private String email;
    private String code;

    // 생성자, 게터, 세터 생략
    public VerificationRequest() {}

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
