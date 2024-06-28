package com.ezen.weather.kakao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class KakaoUserInfoService {

    @Autowired
    private KakaoUserInfoRepository repository;

    // 모든 사용자 정보 조회
    public List<KakaoUserInfo> getAllUsers() {
        return repository.findAll();
    }

    // 이메일로 사용자 정보 조회
    public Optional<KakaoUserInfo> getUserByEmail(String email) {
        return repository.findById(email);
    }

    // 사용자 정보 저장
    public KakaoUserInfo saveUser(KakaoUserInfo user) {
        return repository.save(user);
    }

    // 사용자 정보 삭제
    public void deleteUser(String email) {
        repository.deleteById(email);
    }
}
