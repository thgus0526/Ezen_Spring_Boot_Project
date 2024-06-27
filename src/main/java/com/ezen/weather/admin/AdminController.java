package com.ezen.weather.admin;

import com.ezen.weather.user.SiteUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/admin")
public class AdminController {
private final AdminService adminService;

    @GetMapping("/adminPage")
    public String adminPage(Model model) {
        List<SiteUser> userList=adminService.getAllUsers();
        model.addAttribute("userList", userList);
        return "admin_page";
    }

    @PostMapping("/deleteUser")
    @ResponseBody
    public String deleteUser(@RequestParam("userId") String userId) {
        try {
            adminService.deleteUser(userId);
            return "삭제가 성공적으로 수행되었습니다.";
        } catch (Exception e) {
            return "삭제 도중 오류가 발생했습니다: " + e.getMessage();
        }
    }
}
