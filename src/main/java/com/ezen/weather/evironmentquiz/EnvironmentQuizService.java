package com.ezen.weather.evironmentquiz;

import com.ezen.weather.user.SiteUser;
import com.ezen.weather.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EnvironmentQuizService {

    @Autowired
    private EnvironmentQuizRepository quizRepository;
    private UserRepository userRepository;

    @Autowired
    public EnvironmentQuizService(EnvironmentQuizRepository quizRepository, UserRepository userRepository) {
        this.quizRepository = quizRepository;
        this.userRepository = userRepository;
    }

    public List<EnvironmentQuiz> getAllQuizzes() {
        return quizRepository.findAll();
    }

    public EnvironmentQuiz getQuizById(Long id) {
        return quizRepository.findById(id).orElse(null);
    }

    public EnvironmentQuiz saveQuiz(EnvironmentQuiz quiz) {
        return quizRepository.save(quiz);
    }

    public void deleteQuiz(Long id) {
        quizRepository.deleteById(id);
    }

    public void plusPoint(SiteUser user, int point) {
        user.setPoint(user.getPoint() + point);
        userRepository.save(user);
    }

    public void minusPoint(SiteUser user, int point) {
        user.setPoint(user.getPoint() - point);
        userRepository.save(user);
    }
}
