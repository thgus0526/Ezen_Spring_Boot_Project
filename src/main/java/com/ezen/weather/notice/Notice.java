package com.ezen.weather.notice;

import com.ezen.weather.user.SiteUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="notice")
public class Notice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="notice_number")
    private Long noticeNumber;

    @Column(name="notice_title")
    private String noticeTitle;

    @Column(name="notice_content")
    private String noticeContent;

    @Column(name="notice_hit")
    private int noticeHit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private SiteUser adminID;

}
