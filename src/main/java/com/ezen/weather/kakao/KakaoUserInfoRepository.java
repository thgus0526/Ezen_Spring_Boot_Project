package com.ezen.weather.kakao;

import org.springframework.data.jpa.repository.JpaRepository;

public interface KakaoUserInfoRepository extends JpaRepository<KakaoUserInfo, String> {
    // 추가적인 쿼리 메서드 선언 가능
}
