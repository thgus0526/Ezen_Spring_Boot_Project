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

        if(siteUser.isPresent()) {
            return siteUser.get();
        } else {
            throw new DataNotFoundException("siteuser not found");
        }
    }

}


