package com.ezen.weather.answer;

import com.ezen.weather.question.Question;
import com.ezen.weather.user.SiteUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "answer") // 데이터베이스 테이블 이름 지정
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @Column(name = "create_date")
    private LocalDateTime createDate;

    @ManyToOne
    @JoinColumn(name = "question_id") // 외래 키 이름 지정
    private Question question;

    @ManyToOne
    @JoinColumn(name = "author_id") // 외래 키 이름 지정
    private SiteUser author;

    @Column(name = "modify_date")
    private LocalDateTime modifyDate;
}