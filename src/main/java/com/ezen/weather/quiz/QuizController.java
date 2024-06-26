package com.ezen.weather.quiz;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class QuizController {
    @GetMapping("/user/quiz")
    public String quiz() {
        return "quiz";
    }
}
