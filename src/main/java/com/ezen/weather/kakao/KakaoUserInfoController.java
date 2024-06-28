package com.ezen.weather.kakao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/kakao-users")
public class KakaoUserInfoController {

    @Autowired
    private KakaoUserInfoService service;

    // 모든 사용자 정보 조회
    @GetMapping
    public List<KakaoUserInfo> getAllUsers() {
        return service.getAllUsers();
    }

    // 이메일로 사용자 정보 조회
    @GetMapping("/{email}")
    public ResponseEntity<KakaoUserInfo> getUserByEmail(@PathVariable String email) {
        Optional<KakaoUserInfo> user = service.getUserByEmail(email);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 새로운 사용자 정보 저장
    @PostMapping
    public KakaoUserInfo createUser(@RequestBody KakaoUserInfo user) {
        return service.saveUser(user);
    }

    // 사용자 정보 업데이트
    @PutMapping("/{email}")
    public ResponseEntity<KakaoUserInfo> updateUser(@PathVariable String email, @RequestBody KakaoUserInfo userDetails) {
        Optional<KakaoUserInfo> user = service.getUserByEmail(email);

        if (user.isPresent()) {
            KakaoUserInfo updatedUser = user.get();
            updatedUser.setNickname(userDetails.getNickname());
            updatedUser.setProfileImage(userDetails.getProfileImage());
            updatedUser.setName(userDetails.getName());
            updatedUser.setPhoneNumber(userDetails.getPhoneNumber());
            return ResponseEntity.ok(service.saveUser(updatedUser));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // 사용자 정보 삭제
    @DeleteMapping("/{email}")
    public ResponseEntity<Void> deleteUser(@PathVariable String email) {
        service.deleteUser(email);
        return ResponseEntity.noContent().build();
    }
}
