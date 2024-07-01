package com.ezen.weather.admin;

import com.ezen.weather.adminTemp.AdminTemp;
import com.ezen.weather.adminTemp.AdminTempService;
import com.ezen.weather.user.SiteUser;
import com.ezen.weather.user.UserRepository;
import com.ezen.weather.user.UserRole;
import com.ezen.weather.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/admin")
public class AdminController {
private final AdminService adminService;
    private final UserRepository userRepository;
    private final UserService userService;
    private final AdminTempService adminTempService;

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
    @GetMapping("/editUser")
    public String editUserPage(@RequestParam("userId") String userId, Model model) {
        SiteUser siteUser = adminService.getUser(userId);
        model.addAttribute("siteUser", siteUser);
        return "admin_edit_user"; // edit_user.html 템플릿으로 이동
    }

    // 회원정보 수정
    @PostMapping("/editUserInfo")
    public String updateUserInfo(@RequestParam("hiddenName") String name,
                                 @RequestParam("phone") String phone,
                                 @RequestParam("addressZipcode") String addressZipcode,
                                 @RequestParam("addressStreet") String addressStreet,
                                 @RequestParam("addressDetail") String addressDetail,
                                 @RequestParam("addressNotes") String addressNotes) {

        SiteUser siteUser = userRepository.findByName(name);
        adminService.editUserInfo(name, phone, addressZipcode, addressStreet, addressDetail, addressNotes);
        return "redirect:/admin/adminPage";
    }
    // 비밀번호 수정
    @PostMapping("/editPassword")
    public String updateUserPwd(@RequestParam("password") String password, @RequestParam("hiddenName") String name){
        userService.userPwdUpdate(password, name);
        return "redirect:/admin/adminPage";
    }

    @GetMapping("/alarmPage")
    public String alarmPage(Model model) {
        List<AdminTemp> adminTemps = adminTempService.getAllAdminTemps();
        List<SiteUser> userList=userRepository.findAll();

        model.addAttribute("userList", userList);
        model.addAttribute("adminTemps", adminTemps);
        return "alram_page";
    }

    @PostMapping("/adminTempSet")
    public String adminTempSet(@RequestParam("hiddenMaxTemp") String hiddenMaxTemp,
                               @RequestParam("hiddenMinTemp") String hiddenMinTemp,
                               @RequestParam(value="rain", defaultValue="false") boolean rain,
                               @RequestParam("hiddenId") String id) {

        System.out.println(hiddenMaxTemp);

        adminTempService.input(hiddenMaxTemp, hiddenMinTemp, rain, id);

        return "redirect:/";
    }

}
