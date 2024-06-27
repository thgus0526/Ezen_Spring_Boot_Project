package com.ezen.weather.evironmentquiz;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EnvironmentQuizService {

    @Autowired
    private EnvironmentQuizRepository repository;

    public List<EnvironmentQuiz> getAllQuizzes() {
        return repository.findAll();
    }

    public EnvironmentQuiz getQuizById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public EnvironmentQuiz saveQuiz(EnvironmentQuiz quiz) {
        return repository.save(quiz);
    }

    public void deleteQuiz(Long id) {
        repository.deleteById(id);
    }
}
