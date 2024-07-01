package com.ezen.weather.main;

import com.ezen.weather.adminTemp.AdminTemp;
import com.ezen.weather.adminTemp.AdminTempRepository;
import com.ezen.weather.adminTemp.AdminTempService;
import com.ezen.weather.evironmentquiz.EnvironmentQuiz;
import com.ezen.weather.user.UserService;
import com.ezen.weather.userTemp.UserTempService;
import org.springframework.security.access.prepost.PreAuthorize;
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

import java.util.List;
import java.util.Random;

@Controller
public class MainController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;
    @Autowired
    private AdminTempRepository adminTempRepository;
    @Autowired
    private AdminTempService adminTempService;

    @GetMapping("/")
    public String index(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        if (userDetails != null) {
            // 현재 인증된 사용자 정보를 가져옴
            String userId = userDetails.getUsername();
            SiteUser user = userService.getUser(userId);
            String role = userDetails.getAuthorities().iterator().next().getAuthority(); // 로그인한 유저가 일반 유저면 USER, 관리자면 ADMIN 반환
            System.out.println("역할 = " +role);
            List<AdminTemp> adminTemp = adminTempService.getAllAdminTemps();

            System.out.println("user :" + user);
            model.addAttribute("role", role);
            model.addAttribute("user", user);
            model.addAttribute("adminTemp", adminTemp);
            System.out.println(user.getAddressStreet());

        }
        return "index";
    }


}
