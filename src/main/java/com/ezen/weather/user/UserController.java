package com.ezen.weather.user;

import com.ezen.weather.adminTemp.AdminTemp;
import com.ezen.weather.adminTemp.AdminTempService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final AdminTempService adminTempService;


    @GetMapping("/signup")
    public String signup(Model model){
        model.addAttribute("siteUser", new SiteUser());
        return "signup_form";
    }

    @PostMapping("/signup")
    public String signup(@ModelAttribute SiteUser siteUser, Model model){

        userService.create(siteUser.getUserId(), siteUser.getPassword(), siteUser.getName(), siteUser.getPhone(), siteUser.getAddressStreet(), siteUser.getAddressZipcode(), siteUser.getAddressDetail(), siteUser.getAddressNotes(), siteUser.getEmail(), siteUser.getBirth());
        return "redirect:/";
    }
    // 아이디 중복 검사
    @GetMapping("/checkUserId")
    public ResponseEntity<?> checkUserIdAvailability(@RequestParam("userId") String userId){
        boolean available = !userService.isUserIdExists(userId);

        return ResponseEntity.ok().body(Collections.singletonMap("available", available));

    }

    @GetMapping("/checkUserEmail")
    public ResponseEntity<?> checkUserEmailAvailability(@RequestParam("email") String email){
        System.out.println("Controller~~~~~~~~~~~");
        boolean available = !userService.isUserEmailExists(email);
        return ResponseEntity.ok().body(Collections.singletonMap("available", available));
    }

    @GetMapping("/login")
    public String login(Model model){
        model.addAttribute("siteUser", new SiteUser());
        return "login_form";
    }

    @GetMapping("/mypage")
    public String mypage(Model model){
        List<AdminTemp> adminTemps = adminTempService.getAllAdminTemps();
        model.addAttribute("siteUser", new SiteUser());
        model.addAttribute("adminTemps", adminTemps);
        return "user_mypage";

    }
    @PostMapping("/userTempSet")
    public String userTempSet(@RequestParam("hiddenTemp") String hiddenTemp){
        System.out.println(hiddenTemp);

        return "redirect:/";
    }

}
