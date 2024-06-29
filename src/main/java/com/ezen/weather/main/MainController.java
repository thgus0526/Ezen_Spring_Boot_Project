package com.ezen.weather.main;

import com.ezen.weather.user.UserService;
import com.ezen.weather.userTemp.UserTempService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired; // 추가
import com.ezen.weather.user.SiteUser; // 추가
import com.ezen.weather.user.UserRepository;

@Controller
public class MainController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String index(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        if (userDetails != null) {
            // 현재 인증된 사용자 정보를 가져옴
            String userId = userDetails.getUsername();
            SiteUser user = userService.getUser(userId);
            System.out.println("user :" + user);
            model.addAttribute("user", user);
            System.out.println(user.getAddressStreet());

        }
        return "index";
    }
}
