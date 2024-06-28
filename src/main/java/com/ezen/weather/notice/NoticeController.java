package com.ezen.weather.notice;

import com.ezen.weather.DataNotFoundException;
import com.ezen.weather.answer.AnswerForm;
import com.ezen.weather.question.Question;
import com.ezen.weather.question.QuestionForm;
import com.ezen.weather.user.SiteUser;
import com.ezen.weather.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Controller
@RequiredArgsConstructor
@RequestMapping("/notice")
public class NoticeController {
    private final NoticeService noticeService;
    private final UserService userService;
    private final NoticeRepository noticeRepository;

    @GetMapping("list")
    public String noticeList(Model model) {

        List<Notice> noticeList = this.noticeService.getList();
        model.addAttribute("noticeList", noticeList);
        return "notice_list";
    }
    @GetMapping(value = "/detail/{id}")
    public String detail(Model model, @PathVariable("id") Long noticeNumber) {
        Notice notice = this.noticeService.getNotice(noticeNumber);
        model.addAttribute("notice", notice);

        // 이전 페이지와 다음 페이지 정보 가져오기
        Notice previousNotice = this.noticeService.getPreviousNotice(noticeNumber);
        Notice nextNotice = this.noticeService.getNextNotice(noticeNumber);

        // 조회수 증가
        this.noticeService.increaseNoticeHit(noticeNumber);

        model.addAttribute("previousNotice", previousNotice);
        model.addAttribute("nextNotice", nextNotice);

        return "notice_detail";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    public String noticeCreate(NoticeForm noticeForm) {
        return "notice_form";
    }

    @PreAuthorize("isAuthenticated()")// 로그아웃상태에서 호출하면 로그인페이지로 강제 이동.
    @PostMapping("/create")
    public String noticeCreate(@Valid NoticeForm noticeForm, BindingResult bindingResult, Principal principal) {
        if (bindingResult.hasErrors()) {
            return "notice_form";
        }
        SiteUser siteUser = this.userService.getUser(principal.getName());
        this.noticeService.create(noticeForm.getNoticeTitle(), noticeForm.getNoticeContent(), siteUser);
        return "redirect:/notice/list";   // 질문 저장후 질문 목록으로 이동
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String noticeModify(NoticeForm noticeForm, @PathVariable("id") Long noticeNumber) {
        Notice notice = this.noticeService.getNotice(noticeNumber);
        noticeForm.setNoticeTitle(notice.getNoticeTitle());
        noticeForm.setNoticeContent(notice.getNoticeContent());
        return "notice_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String noticeModify(@Valid NoticeForm noticeForm, BindingResult bindingResult,
                                 @PathVariable("id") Long noticeNumber) {
        if (bindingResult.hasErrors()) {
            return "notice_form";
        }
        Notice notice = this.noticeService.getNotice(noticeNumber);
        this.noticeService.modify(notice, noticeForm.getNoticeTitle(),
                noticeForm.getNoticeContent());
        return String.format("redirect:/question/detail/%s", noticeNumber);

    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String noticeDelete(Principal principal, @PathVariable("id") Long noticeNumber) {
        Notice notice = this.noticeService.getNotice(noticeNumber);
        if (!notice.getAdminId().getUserId().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }
        this.noticeService.delete(notice);
        return "redirect:/";
    }
}
