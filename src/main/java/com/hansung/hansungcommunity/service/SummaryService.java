package com.hansung.hansungcommunity.service;


import com.hansung.hansungcommunity.dto.user.UserSummaryDto;
import com.hansung.hansungcommunity.entity.Summary;
import com.hansung.hansungcommunity.entity.User;
import com.hansung.hansungcommunity.exception.InvalidAccessException;
import com.hansung.hansungcommunity.exception.SummaryNotFoundException;
import com.hansung.hansungcommunity.exception.UserNotFoundException;
import com.hansung.hansungcommunity.repository.SummaryRepository;
import com.hansung.hansungcommunity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SummaryService {

    private final UserRepository userRepository;
    private final SummaryRepository summaryRepository;

    @Transactional
    public UserSummaryDto postSummary(Long id, UserSummaryDto dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("공부 요약 작성 실패, 해당하는 유저가 없습니다."));
        Summary summary = Summary.of(dto);
        summary.setUser(user);

        return UserSummaryDto.of(summaryRepository.save(summary));
    }

    public List<UserSummaryDto> getSummary(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("공부 요약 조회 실패, 해당 유저가 없습니다."));

        return summaryRepository.findAllByUser(user)
                .stream()
                .map(UserSummaryDto::of)
                .sorted(Comparator.comparing(UserSummaryDto::getDate).reversed())
                .collect(Collectors.toList());
    }

    public List<UserSummaryDto> getFixedSummary(Long userId) {
        return summaryRepository.findAllByUserIdAndIsFixedTrueOrderByCreatedAtDesc(userId)
                .stream().map(UserSummaryDto::of).collect(Collectors.toList());
    }

    @Transactional
    public void deleteSummary(Long summaryId) {
        Summary summary = summaryRepository.findById(summaryId)
                .orElseThrow(() -> new SummaryNotFoundException("공부 요약 삭제 실패, 해당하는 공부 요약이 없습니다."));

        summaryRepository.delete(summary);
    }

    @Transactional
    public UserSummaryDto updateSummary(Long summaryId, UserSummaryDto dto) {
        Summary summary = summaryRepository.findById(summaryId)
                .orElseThrow(() -> new SummaryNotFoundException("공부 요약 수정 실패, 해당하는 공부 요약이 없습니다."));
        summary.updateContent(dto.getContent());

        return UserSummaryDto.of(summary);
    }

    /**
     * 오늘의 한줄 고정
     */
    @Transactional
    public void fixSummary(Long summaryId, Long userId) {
        Summary summary = summaryRepository.findById(summaryId)
                .orElseThrow(() -> new SummaryNotFoundException("공부 요약 고정 실패, 해당하는 공부 요약이 없습니다."));

        if (!summary.getUser().getId().equals(userId)) {
            throw new InvalidAccessException("공부 요약 고정 실패, 작성자가 아닙니다.");
        }

        summary.fix();
    }

    /**
     * 오늘의 한줄 고정 해제
     */
    @Transactional
    public void releaseSummary(Long summaryId, Long userId) {
        Summary summary = summaryRepository.findById(summaryId)
                .orElseThrow(() -> new SummaryNotFoundException("공부 요약 고정 해제 실패, 해당하는 공부 요약이 없습니다."));

        if (!summary.getUser().getId().equals(userId)) {
            throw new InvalidAccessException("공부 요약 고정 해제 실패, 작성자가 아닙니다.");
        }

        summary.release();
    }

}
