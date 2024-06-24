package com.ezen.weather.main;

import com.ezen.weather.user.SiteUser;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@AllArgsConstructor
@Controller
public class MainController {
    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("siteUser", new SiteUser());
        return "index";
    }
}
