package com.ezen.weather.admin;

import com.ezen.weather.DataNotFoundException;
import com.ezen.weather.user.SiteUser;
import com.ezen.weather.user.UserRepository;
import com.ezen.weather.user.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AdminService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public List<SiteUser> getAllUsers() {
        return userRepository.findAll();
    }

    public void deleteUser(String userId) {
        userRepository.deleteById(userId);
    }

    // 회원정보 수정
    public void userInfoModify(String name, String phone, String addressZipcode, String addressStreet, String addressDetail, String addressNotes, String email) {
        SiteUser siteUser = userRepository.findByName(name);

        siteUser.setPassword(passwordEncoder.encode(siteUser.getPassword()));
        siteUser.setPhone(phone);
        siteUser.setAddressZipcode(addressZipcode);
        siteUser.setAddressStreet(addressStreet);
        siteUser.setAddressDetail(addressDetail);
        siteUser.setAddressNotes(addressNotes);
        siteUser.setEmail(email);
        this.userRepository.save(siteUser);

    }

    public SiteUser getUser(String userId) {
        Optional<SiteUser> siteUser = userRepository.findByUserId(userId);
        if (siteUser.isPresent()) {
            return siteUser.get();
        } else {
            throw new DataNotFoundException("사용자를 찾을 수 없습니다.");
        }
    }

    // 회원정보 수정
    public void editUserInfo(String name, String phone, String addressZipcode, String addressStreet, String addressDetail, String addressNotes, UserRole role) {
        SiteUser siteUser = userRepository.findByName(name);

        siteUser.setPhone(phone);
        siteUser.setAddressZipcode(addressZipcode);
        siteUser.setAddressStreet(addressStreet);
        siteUser.setAddressDetail(addressDetail);
        siteUser.setAddressNotes(addressNotes);
        siteUser.setRole(role);
        this.userRepository.save(siteUser);

    }
    // 비밀번호 수정
    public void userPwdUpdate(String password, String name){
        SiteUser siteUser = userRepository.findByName(name);

        siteUser.setPassword(passwordEncoder.encode(password));
        this.userRepository.save(siteUser);
    }
}
