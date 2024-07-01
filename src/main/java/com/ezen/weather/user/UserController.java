package com.ezen.weather.user;

import com.ezen.weather.adminTemp.AdminTemp;
import com.ezen.weather.adminTemp.AdminTempService;
import com.ezen.weather.email.EmailService;

import com.ezen.weather.userTemp.UserTemp;
import com.ezen.weather.userTemp.UserTempService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final AdminTempService adminTempService;
    private final UserTempService userTempService;
    private final UserRepository userRepository;


    @GetMapping("/signup")
    public String signup(Model model) {
        model.addAttribute("siteUser", new SiteUser());
        return "signup_form";
    }

    @PostMapping("/signup")
    public String signup(@ModelAttribute SiteUser siteUser, Model model) {

        userService.create(siteUser.getUserId(), siteUser.getPassword(), siteUser.getName(), siteUser.getPhone(), siteUser.getAddressStreet(), siteUser.getAddressZipcode(), siteUser.getAddressDetail(), siteUser.getAddressNotes(), siteUser.getEmail(), siteUser.getBirth());
        return "redirect:/";
    }

    // 아이디 중복 검사
    @GetMapping("/checkUserId")
    public ResponseEntity<?> checkUserIdAvailability(@RequestParam("userId") String userId) {
        boolean available = !userService.isUserIdExists(userId);

        return ResponseEntity.ok().body(Collections.singletonMap("available", available));

    }

    @GetMapping("/checkUserEmail")
    public ResponseEntity<?> checkUserEmailAvailability(@RequestParam("email") String email) {
        System.out.println("Controller~~~~~~~~~~~");
        boolean available = !userService.isUserEmailExists(email);
        return ResponseEntity.ok().body(Collections.singletonMap("available", available));
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("siteUser", new SiteUser());
        return "login_form";
    }

    @GetMapping("/mypage")
    public String mypage(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        String userId = userDetails.getUsername();
        UserTemp userTemp = userTempService.getUserTemp(userId);
        System.out.println("유저 템프값 : " +userTemp);

        List<AdminTemp> adminTemps = adminTempService.getAllAdminTemps();
        SiteUser siteUser = userService.getUser(userId);

        model.addAttribute("siteUser", siteUser);
        model.addAttribute("userTemp", userTemp);
        model.addAttribute("adminTemps", adminTemps);
        return "user_mypage";

    }

    @PostMapping("/userTempSet")
    public String userTempSet(@RequestParam("hiddenMaxTemp") String hiddenMaxTemp,
                              @RequestParam("hiddenMinTemp") String hiddenMinTemp,
                              @RequestParam(value = "rain", defaultValue = "false") boolean rain,
                              @RequestParam("hiddenUserId") String hiddenUserId) {
        System.out.println(rain);
        System.out.println(hiddenUserId);


        userTempService.input(hiddenMaxTemp, hiddenMinTemp, rain, hiddenUserId);


        return "redirect:/";
    }

    // 회원정보 수정
    @PostMapping("/updateUserInfo")
    public String updateUserInfo(@RequestParam("hiddenName") String name,
                                 @RequestParam("phone") String phone,
                                 @RequestParam("addressZipcode") String addressZipcode,
                                 @RequestParam("addressStreet") String addressStreet,
                                 @RequestParam("addressDetail") String addressDetail,
                                 @RequestParam("addressNotes") String addressNotes,
                                 @RequestParam("email") String email) {

        SiteUser siteUser = userRepository.findByName(name);
        userService.userInfoUpdate(name, phone, addressZipcode, addressStreet, addressDetail, addressNotes, email);
        return "redirect:/user/mypage";
    }
    // 비밀번호 수정
    @PostMapping("/updatePassword")
    public ResponseEntity<String> updateUserPwd(@RequestParam("password") String password, @RequestParam("hiddenName") String name){
        userService.userPwdUpdate(password, name);
        return ResponseEntity.ok("Password updated successfully");
    }


    // 포인트 전환
    @PostMapping("/exchangePointToSms")
    public ResponseEntity<?> exchangePointToSms(@RequestBody Map<String, Object> data){
        String userId = (String) data.get("userId");
        Integer exchangePoint = Integer.parseInt((String)data.get("point"));
        long exchangePointL = exchangePoint;
        SiteUser siteUser = userService.getUser(userId);
        long currentPoint = siteUser.getPoint();

        if(exchangePoint > currentPoint){
            return ResponseEntity.ok("포인트가 교환할 값보다 적습니다.");
        } else{
            userService.exchangePointToSms(userId, exchangePointL);
            return ResponseEntity.ok("포인트 교환 성공!");
        }



    }

    @PostMapping("/updatePoint")
    public ResponseEntity<?> updatePoint(@RequestBody Map<String, Object> data){
        String userId = (String) data.get("userId");
        String userPoint = data.get("userPoint").toString();
        System.out.println("유저아이디" + userId);
        System.out.println("유저포인트" + userPoint);
        userService.updatePoint(userId);


        return ResponseEntity.ok("Point updated successfully");
    }

    // 회원탈퇴
    @PostMapping("/deleteUser")
    public ResponseEntity<?> deleteUser(@RequestBody Map<String, Object> data) {
        System.out.println("컨트롤러 진입");
        String userId = (String) data.get("userId");
        String pwd = (String) data.get("password");
        
        System.out.println(userId);
        System.out.println(pwd);

        userService.deleteUser(userId, pwd);

        return ResponseEntity.ok("회원탈퇴 성공");
    }

}
