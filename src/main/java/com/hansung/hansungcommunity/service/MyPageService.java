package com.hansung.hansungcommunity.service;

import com.hansung.hansungcommunity.dto.user.UserActivityDto;
import com.hansung.hansungcommunity.dto.user.UserUpdateDto;
import com.hansung.hansungcommunity.entity.*;
import com.hansung.hansungcommunity.exception.DuplicateNicknameException;
import com.hansung.hansungcommunity.exception.SkillNotFoundException;
import com.hansung.hansungcommunity.exception.UserNotFoundException;
import com.hansung.hansungcommunity.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MyPageService {

    private final FreeReplyRepository freeReplyRepository;
    private final QnaReplyRepository qnaReplyRepository;
    private final RecruitReplyRepository recruitReplyRepository;
    private final BoardRepository boardRepository;
    private final BookmarkRepository bookmarkRepository;
    private final UserRepository userRepository;
    private final SkillRepository skillRepository;

    /**
     * 해당 유저가 댓글 단 게시글을 최신 순으로
     */
    public List<UserActivityDto> getReplyList(Long userId) {
        Set<UserActivityDto> userActivityDtos = new HashSet<>();
        List<FreeReply> freeReplies = freeReplyRepository.findByUserId(userId);
        for (FreeReply freeReply : freeReplies) {
            userActivityDtos.add(UserActivityDto.of(freeReply.getFreeBoard()));
        }
        List<QnaReply> qnaReplies = qnaReplyRepository.findByUserId(userId);
        for (QnaReply qnaReply : qnaReplies) {
            userActivityDtos.add(UserActivityDto.of(qnaReply.getBoard()));
        }
        List<RecruitReply> recruitReplies = recruitReplyRepository.findByUserId(userId);
        for (RecruitReply recruitReply : recruitReplies) {
            userActivityDtos.add(UserActivityDto.of(recruitReply.getRecruitBoard()));
        }
        List<UserActivityDto> sortedList = new ArrayList<>(userActivityDtos);
        sortedList.sort(Comparator.comparing(UserActivityDto::getCreatedDate).reversed());
        return sortedList;
    }

    /**
     * 해당 접속 유저가 작성한 게시글 반환 (최신순서)
     */
    public List<UserActivityDto> getBoardList(Long userId) {
        return boardRepository.findAllByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(UserActivityDto::of)
                .collect(Collectors.toList());
    }

    /**
     * 해당 접속 유저가 북마크한 게시글 반환 (최신순서)
     */
    public List<UserActivityDto> getBookmarkList(Long userId) {
        List<Bookmark> bookmarks = bookmarkRepository.findAllByUserId(userId);

        return bookmarks.stream()
                .map(bookmark -> UserActivityDto.of(bookmark.getBoard()))
                .sorted(Comparator.comparing(UserActivityDto::getCreatedDate).reversed())
                .collect(Collectors.toList());
    }

    /**
     * 유저 자기소개, 관심 기술 수정
     */
    @Transactional
    public void updateUserInfo(UserUpdateDto dto, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("유저 정보 수정 실패, 해당 유저가 없습니다."));

        if (!user.getNickname().equals(dto.getNickname())) validateDuplicateNickname(dto.getNickname());

        Set<Skill> skills = dto.getSkills().stream().map(s -> skillRepository.findByName(s)
                        .orElseThrow(() -> new SkillNotFoundException("관심 기술 수정 실패, 해당하는 기술이 없습니다.")))
                .collect(Collectors.toSet());

        user.updateUserInfo(dto);
        user.setSkills(skills);
    }

    private void validateDuplicateNickname(String nickname) {
        if (userRepository.existsUserByNickname(nickname)) throw new DuplicateNicknameException("이미 사용 중인 닉네임입니다.");
    }

}
