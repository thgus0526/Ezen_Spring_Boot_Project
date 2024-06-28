package com.ezen.weather.notice;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
    // 이전 공지사항 조회
    Notice findTopByNoticeNumberLessThanOrderByNoticeNumberDesc(Long noticeNumber);

    // 다음 공지사항 조회
    Notice findTopByNoticeNumberGreaterThanOrderByNoticeNumber(Long noticeNumber);
}
