package com.ezen.weather.evironmentquiz;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface EnvironmentQuizRepository extends JpaRepository<EnvironmentQuiz, Long> {
}