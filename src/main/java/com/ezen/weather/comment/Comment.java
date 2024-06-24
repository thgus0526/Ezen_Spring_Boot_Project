package com.ezen.weather.comment;

import com.ezen.weather.notice.Notice;
import com.ezen.weather.user.SiteUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="comment")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="comment_number")
    private Long commentNumber;

    @Column(name="comment_content")
    private String commentContent;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="notice_number")
    private Notice notice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private SiteUser siteUser;
}
