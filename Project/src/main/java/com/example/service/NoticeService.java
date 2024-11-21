package com.example.service;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import lombok.RequiredArgsConstructor;

import com.example.model.Notice;
import com.example.repository.NoticeRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NoticeService {
    
    private final NoticeRepository noticeRepository;
    
    public List<Notice> getAllNotices() {
        return noticeRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }
    
    @Transactional
    public void increaseViewCount(Long id) {
        Notice notice = noticeRepository.findById(id).orElse(null);
        if (notice != null) {
            notice.setViewCount(notice.getViewCount() + 1);
            noticeRepository.save(notice);
        }
    }
    
    @Transactional
    public void saveNotice(Notice notice) {
        noticeRepository.save(notice);
    }
    
    @Transactional(readOnly = true)
    public Notice getNotice(Long id) {
        Notice notice = noticeRepository.findById(id).orElse(null);
        System.out.println("Found notice: " + notice);
        return notice;
    }
    
    @Transactional
    public void deleteNotice(Long id) {
        noticeRepository.deleteById(id);
    }
    
    @Transactional
    public void updateNotice(Long id, Notice updateNotice) {
        Notice notice = noticeRepository.findById(id).orElseThrow();
        notice.setTitle(updateNotice.getTitle());
        notice.setContent(updateNotice.getContent());
        // 나머지 필드는 유지
    }
} 