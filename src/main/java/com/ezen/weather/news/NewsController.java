package com.ezen.weather.news;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@AllArgsConstructor
public class NewsController {
    @GetMapping("/user/news")
    public String news() {
        return "news";
    }
}
