package com.ezen.weather.notice;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class NoticeService {
    private final NoticeRepository noticeRepository;
    public List<Notice> getList(){
        return this.noticeRepository.findAll();
    }
}
