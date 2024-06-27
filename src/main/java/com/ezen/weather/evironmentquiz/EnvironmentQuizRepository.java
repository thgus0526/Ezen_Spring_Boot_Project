package com.ezen.weather.evironmentquiz;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface EnvironmentQuizRepository extends JpaRepository<EnvironmentQuiz, Long> {
    // 추가적인 메소드가 필요하면 여기에 정의 가능
}
