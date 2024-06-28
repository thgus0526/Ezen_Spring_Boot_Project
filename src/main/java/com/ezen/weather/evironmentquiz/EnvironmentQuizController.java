package com.ezen.weather.evironmentquiz;
import com.ezen.weather.evironmentquiz.EnvironmentQuiz;
import com.ezen.weather.evironmentquiz.EnvironmentQuizService;
import com.ezen.weather.user.SiteUser;
import com.ezen.weather.user.UserRepository;
import com.ezen.weather.user.UserService;
import org.apache.maven.model.Site;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Controller
public class EnvironmentQuizController {

    private final EnvironmentQuizService quizService;
    private final UserService userService;


    private final AuthenticationManager authenticationManager;

    @Autowired
    public EnvironmentQuizController(EnvironmentQuizService quizService, UserService userService, AuthenticationManager authenticationManager) {
        this.quizService = quizService;
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }

    @GetMapping("/quiz")
    public String getQuiz(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        List<EnvironmentQuiz> quizList = quizService.getAllQuizzes();
        if (quizList.isEmpty()) {
            model.addAttribute("error", "퀴즈가 존재하지 않습니다.");
            return "error"; // error.html로 리디렉션
        }
        Random rand = new Random();
        EnvironmentQuiz randomQuiz = quizList.get(rand.nextInt(quizList.size()));
        String userId = userDetails.getUsername();
        SiteUser user = userService.getUser(userId);

        model.addAttribute("quiz", randomQuiz);
        model.addAttribute("user", user);
        return "quiz";

    }

    @PostMapping("/quiz")
    public ResponseEntity<?> submitAnswer(@RequestParam boolean answer, @RequestParam Long quizId, @RequestParam String userId, Model model) {
        System.out.println("컨트롤러 시작");
        EnvironmentQuiz quiz = quizService.getQuizById(quizId);
        SiteUser user = userService.getUser(userId);
        System.out.println("userId: " + userId);
        System.out.println("SiteUser: " + user);
        if (quiz == null) {
//            model.addAttribute("error", "퀴즈를 찾을 수 없습니다.");
//            return "error"; // error.html로 리디렉션
            return ResponseEntity.badRequest().body("퀴즈를 찾을 수 없습니다.");
        }
        boolean isCorrect = (quiz.getAnswer() == (answer ? 1 : 0));

        if(isCorrect) {
            quizService.plusPoint(user, quiz.getPoint());
        } else {
            quizService.minusPoint(user, quiz.getPoint());
        }

        System.out.println(isCorrect);

        // JSON 응답을 위한 맵 구성
        Map<String, Object> response = new HashMap<>();
        response.put("isCorrect", isCorrect);
        response.put("userPoint", user.getPoint());

        return ResponseEntity.ok(response);
    }
}
