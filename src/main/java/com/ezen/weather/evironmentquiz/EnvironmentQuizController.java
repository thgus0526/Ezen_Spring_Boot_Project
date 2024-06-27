package com.ezen.weather.evironmentquiz;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
public class EnvironmentQuizController {
    private final EnvironmentQuizRepository quizRepository;

    @Autowired
    public EnvironmentQuizController(EnvironmentQuizRepository quizRepository) {
        this.quizRepository = quizRepository;
    }

    @GetMapping("/quiz")
    public String getQuiz(Model model) {
        List<EnvironmentQuiz> quizList = quizRepository.findAll();
        model.addAttribute("quizList", quizList);
        return "quiz";
    }
}
