package com.ezen.weather.question;

import com.ezen.weather.answer.Answer;
import com.ezen.weather.user.SiteUser;
import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "question") // 데이터베이스 테이블 이름 지정
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "subject", length = 200)
    private String subject;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @Column(name = "create_date")
    private LocalDateTime createDate;

    @OneToMany(mappedBy = "question", cascade = CascadeType.REMOVE)
    private List<Answer> answerList;

    @ManyToOne
    @JoinColumn(name = "author_id") // 외래 키 이름 지정
    private SiteUser author;

    @Column(name = "modify_date")
    private LocalDateTime modifyDate;
}