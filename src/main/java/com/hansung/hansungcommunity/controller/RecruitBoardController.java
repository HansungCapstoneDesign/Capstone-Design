package com.hansung.hansungcommunity.controller;

import com.hansung.hansungcommunity.auth.CustomAuthentication;
import com.hansung.hansungcommunity.dto.recruit.RecruitBoardDetailDto;
import com.hansung.hansungcommunity.dto.recruit.RecruitBoardListDto;
import com.hansung.hansungcommunity.dto.recruit.RecruitBoardRequestDto;
import com.hansung.hansungcommunity.service.RecruitBoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class RecruitBoardController {

    private final RecruitBoardService recruitBoardService;

    /**
     * 게시글 생성
     */
    @PostMapping("/recruit")
    public ResponseEntity<Long> create(@RequestBody RecruitBoardRequestDto dto, Authentication authentication) {
        CustomAuthentication ca = (CustomAuthentication) authentication;
        Long savedId = recruitBoardService.post(ca.getUser().getId(), dto);

        return ResponseEntity.ok(savedId);
    }

    /**
     * 게시글 목록 조회
     */
    @GetMapping("/recruit/list")
    public ResponseEntity<List<RecruitBoardListDto>> list(Pageable pageable) {
        List<RecruitBoardListDto> recruitList = recruitBoardService.getList(pageable);

        return ResponseEntity.ok(recruitList);
    }

    /**
     * 게시글 상세 조회
     */
    @GetMapping("/recruit/detail/{boardId}")
    public ResponseEntity<RecruitBoardDetailDto> detail(
            @PathVariable("boardId") Long boardId,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        increaseHits(boardId, request, response);
        RecruitBoardDetailDto detailDto = recruitBoardService.getDetail(boardId);

        return ResponseEntity.ok(detailDto);
    }

    /**
     * 조회수 증가 로직
     */
    private void increaseHits(Long boardId, HttpServletRequest request, HttpServletResponse response) {
        Cookie oldCookie = null;

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("recruitBoardHits")) {
                    oldCookie = cookie;
                }
            }
        }

        if (oldCookie != null) {
            if (!oldCookie.getValue().contains("[" + boardId.toString() + "]")) {
                recruitBoardService.increaseHits(boardId);
                oldCookie.setValue(oldCookie.getValue() + "_[" + boardId + "]");
                oldCookie.setPath("/");
                oldCookie.setMaxAge(60 * 60 * 24);
                response.addCookie(oldCookie);
            }
        } else {
            recruitBoardService.increaseHits(boardId);
            Cookie newCookie = new Cookie("recruitBoardHits", "[" + boardId + "]");
            newCookie.setPath("/");
            newCookie.setMaxAge(60 * 60 * 24);
            response.addCookie(newCookie);
        }
    }

}
