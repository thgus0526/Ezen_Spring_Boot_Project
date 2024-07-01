package com.ezen.weather.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<SiteUser, String> {
    // 사용자 아이디로 사용자 찾기
    Optional<SiteUser> findByUserId(String userID);

    // 사용자 이메일로 사용자 찾기
    SiteUser findByEmail(String email);

    // 사용자 이름으로 사용자 찾기
    SiteUser findByName(String name);

    SiteUser findByNameAndPhone(String name, String phone);


}
