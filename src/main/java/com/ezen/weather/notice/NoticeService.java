    package com.ezen.weather.notice;

    import com.ezen.weather.DataNotFoundException;
    import com.ezen.weather.question.Question;
    import com.ezen.weather.user.SiteUser;
    import jakarta.transaction.Transactional;
    import lombok.RequiredArgsConstructor;
    import org.springframework.stereotype.Service;

    import java.time.LocalDateTime;
    import java.util.List;
    import java.util.Optional;

    @RequiredArgsConstructor
    @Service
    public class NoticeService {
        private final NoticeRepository noticeRepository;
        public List<Notice> getList(){
            return this.noticeRepository.findAll();
        }
        public Notice getNotice(Long noticeNumber) {
            Optional<Notice> notice = this.noticeRepository.findById(noticeNumber);
            if(notice.isPresent()) {
                return notice.get();
            }else {
                throw new DataNotFoundException("question not found");
            }
        }
        public void create(String subject, String content, SiteUser user) {
            Notice n = new Notice();
            n.setNoticeTitle(subject);
            n.setNoticeContent(content);
            n.setCreateDate(LocalDateTime.now());
            n.setAdminId(user);
            this.noticeRepository.save(n);
        }
        public void modify(Notice notice, String noticeTitle, String noticeContent) {
            notice.setNoticeTitle(noticeTitle);
            notice.setNoticeContent(noticeContent);
            notice.setModifyDate(LocalDateTime.now());
            this.noticeRepository.save(notice);
        }
        public void delete(Notice notice) {
            this.noticeRepository.delete(notice);
        }

        public Notice getPreviousNotice(Long noticeNumber) {
            return noticeRepository.findTopByNoticeNumberLessThanOrderByNoticeNumberDesc(noticeNumber);
        }

        public Notice getNextNotice(Long noticeNumber) {
            return noticeRepository.findTopByNoticeNumberGreaterThanOrderByNoticeNumber(noticeNumber);
        }

        @Transactional
        public void increaseNoticeHit(Long noticeNumber) {
            Notice notice = getNotice(noticeNumber);
            notice.increaseHit();
            noticeRepository.save(notice);
        }


    }
