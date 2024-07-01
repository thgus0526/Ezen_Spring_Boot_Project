package com.ezen.weather.user;

import com.ezen.weather.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public SiteUser create(String userId, String password, String name, String phone, String addressStreet, String addressZipcode, String addressDetail, String addressNotes, String email, String birth) {
        SiteUser user = new SiteUser();
        user.setUserId(userId);
        user.setPassword(passwordEncoder.encode(password));
        user.setName(name);
        user.setPhone(phone);
        user.setAddressStreet(addressStreet);
        user.setAddressZipcode(addressZipcode);
        user.setAddressDetail(addressDetail);
        user.setAddressNotes(addressNotes);
        user.setEmail(email);
        user.setBirth(birth);
        this.userRepository.save(user);
        return user;

    }
    public boolean isUserIdExists(String userId){
        return userRepository.existsById(userId);
    }

    public boolean isUserEmailExists(String email){
        System.out.println("userService email" + email);
        SiteUser user = userRepository.findByEmail(email);
        return user != null;
    }

    public SiteUser getUser(String userId) {
        Optional<SiteUser> siteUser = this.userRepository.findByUserId(userId);
        SiteUser user = userRepository.findByEmail(siteUser.get().getEmail());

        if(siteUser.isPresent()) {
            // 마이페이지에서 유저정보를 뿌릴때 누적포인트에따라 등급을 매긴다.
            if(siteUser.get().getAccPoint()<1000){
                user.setGrade("Bronze");
                this.userRepository.save(user);
            } else if(siteUser.get().getAccPoint()<2000){
                user.setGrade("Silver");
                this.userRepository.save(user);
            } else if(siteUser.get().getAccPoint()<3000){
                user.setGrade("Gold");
                this.userRepository.save(user);
            } else if(siteUser.get().getAccPoint()>=3000){
                user.setGrade("Platinum");
                this.userRepository.save(user);
            }
            return siteUser.get();
        } else {
            throw new DataNotFoundException("siteuser not found");
        }
    }

    // 회원정보 수정
    public void userInfoUpdate(String name, String phone, String addressZipcode, String addressStreet, String addressDetail, String addressNotes, String email) {
        SiteUser siteUser = userRepository.findByName(name);

        siteUser.setPhone(phone);
        siteUser.setAddressZipcode(addressZipcode);
        siteUser.setAddressStreet(addressStreet);
        siteUser.setAddressDetail(addressDetail);
        siteUser.setAddressNotes(addressNotes);
        siteUser.setEmail(email);
        this.userRepository.save(siteUser);

    }
    // 비밀번호 수정
    public void userPwdUpdate(String password, String name){
        SiteUser siteUser = userRepository.findByName(name);

        siteUser.setPassword(passwordEncoder.encode(password));
        this.userRepository.save(siteUser);
    }



    // 포인트 전환
    public void exchangePointToSms(String userId, Long exchangePoint){
        Optional<SiteUser> siteUser = userRepository.findById(userId);
        SiteUser user = userRepository.findByName(siteUser.get().getName());

        // 기존포인트 기존 1000
        long currentPoint = user.getPoint();
        // 기존 문자횟수 5회 무료
        int currentSms = user.getSms();

        // 등급을 받아옴(등급별 할인적용하기위해)
        String grade = siteUser.get().getGrade();

        // 기본 차감값 100 포인트당 문자 1
        long smsPoint = 100;

        // 등급별 할인
        if(grade.equals("Bronze")){
            // 등급별 할인된포인트 값
            double salePoint = smsPoint - (smsPoint*0.05); // 95
            // long 으로 캐스팅
            long salePointL = (long)salePoint;
            // sms 횟수로 전환 브론즈 등급 90포인트당 1건
            int smsCount = (int)(exchangePoint/salePointL);
            // 기존포인트에서 차감
            long afterPoint = currentPoint - salePointL*smsCount; // 905

            int afterSmsCount = currentSms+smsCount; //6

            user.setSms(afterSmsCount); // 5->6
            user.setPoint(afterPoint); // 1000 -> 905
            this.userRepository.save(user);
        } else if(grade.equals("Silver")){
            // 등급별 할인된포인트 값
            double salePoint = smsPoint - (smsPoint*0.1);
            // long 으로 캐스팅
            long salePointL = (long)salePoint;
            // sms 횟수로 전환 실버 등급 85포인트당 1건
            int smsCount = (int)(exchangePoint/salePointL);
            // 기존포인트에서 차감
            long afterPoint = currentPoint - salePointL*smsCount; // 905
            int afterSmsCount = currentSms+smsCount;

            user.setSms(afterSmsCount);
            user.setPoint(afterPoint);
            this.userRepository.save(user);

        } else if(grade.equals("Gold")){
            // 등급별 할인된포인트 값
            double salePoint = smsPoint - (smsPoint*0.15);
            // long 으로 캐스팅
            long salePointL = (long)salePoint;
            // sms 횟수로 전환 골드 등급 80포인트당 1건
            int smsCount = (int)(exchangePoint/salePointL);
            // 기존포인트에서 차감
            long afterPoint = currentPoint - salePointL*smsCount; // 905
            int afterSmsCount = currentSms+smsCount;

            user.setSms(afterSmsCount);
            user.setPoint(afterPoint);
            this.userRepository.save(user);
        } else if(grade.equals("Platinum")){
            // 등급별 할인된포인트 값
            double salePoint = smsPoint - (smsPoint*0.2);
            // long 으로 캐스팅
            long salePointL = (long)salePoint;
            // sms 횟수로 전환 플레티넘 등급 75포인트당 1건
            int smsCount = (int)(exchangePoint/salePointL);
            // 기존포인트에서 차감
            long afterPoint = currentPoint - salePointL*smsCount; // 905
            int afterSmsCount = currentSms+smsCount;

            user.setSms(afterSmsCount);
            user.setPoint(afterPoint);
            this.userRepository.save(user);
        }

    }
    // 문자발송시 문자횟수 차감
    public void discountSmsCount(String userId){
        Optional<SiteUser> siteUser = userRepository.findById(userId);
        SiteUser user = userRepository.findByName(siteUser.get().getName());

        int currentSms = user.getSms();
        int updateCurrentSms = currentSms -1;
        user.setSms(updateCurrentSms);
        this.userRepository.save(user);
    }
    public void updatePoint(String userId){
        Optional<SiteUser> siteUser = userRepository.findById(userId);
        SiteUser user = userRepository.findByName(siteUser.get().getName());

        user.setPoint(50l);
        this.userRepository.save(user);
    }

    // 회원탈퇴
    public void deleteUser(String userId, String pwd){
        Optional<SiteUser> siteUser = userRepository.findById(userId);

        if(siteUser.isPresent()){
            SiteUser user = siteUser.get();
            String storedPassword = user.getPassword();
            // 사용자가 입력한 비밀번호와 데이터베이스에 저장된 비밀번호를 비교
            if(passwordEncoder.matches(pwd, storedPassword)){
                // 비밀번호가 일치시 탈퇴
                userRepository.delete(user);
            } else{
                throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
            }
        } else {
            throw new IllegalArgumentException("사용자를 찾을 수 없습니다.");
        }


    }
    public String findIdByNameAndPhone(String name, String phone) {
        SiteUser user = userRepository.findByNameAndPhone(name, phone);
        return user != null ? user.getUserId() : null;
    }
}


