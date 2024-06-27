package com.ezen.weather.evironmentquiz;
import com.ezen.weather.evironmentquiz.EnvironmentQuiz;
import com.ezen.weather.evironmentquiz.EnvironmentQuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Random;

@Controller
public class EnvironmentQuizController {

    private final EnvironmentQuizService quizService;

    @Autowired
    public EnvironmentQuizController(EnvironmentQuizService quizService) {
        this.quizService = quizService;
    }

    @GetMapping("/quiz")
    public String getQuiz(Model model) {
        List<EnvironmentQuiz> quizList = quizService.getAllQuizzes();
        if (quizList.isEmpty()) {
            model.addAttribute("error", "퀴즈가 존재하지 않습니다.");
            return "error"; // error.html로 리디렉션
        }
        Random rand = new Random();
        EnvironmentQuiz randomQuiz = quizList.get(rand.nextInt(quizList.size()));
        model.addAttribute("quiz", randomQuiz);
        return "quiz";
    }

    @PostMapping("/quiz")
    public String submitAnswer(@RequestParam boolean answer, @RequestParam Long quizId, Model model) {
        EnvironmentQuiz quiz = quizService.getQuizById(quizId);
        if (quiz == null) {
            model.addAttribute("error", "퀴즈를 찾을 수 없습니다.");
            return "error"; // error.html로 리디렉션
        }
        boolean isCorrect = (quiz.getAnswer() == (answer ? 1 : 0));
        model.addAttribute("isAnswer", isCorrect);
        return "result";
    }
}
