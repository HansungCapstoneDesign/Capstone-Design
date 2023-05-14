package com.hansung.hansungcommunity.service;

import com.hansung.hansungcommunity.dto.AdminBoardDto;
import com.hansung.hansungcommunity.dto.AdminUserDto;
import com.hansung.hansungcommunity.entity.Board;
import com.hansung.hansungcommunity.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminService {
    private final FreeBoardService freeBoardService;
    private final QnaBoardService qnaBoardService;
    private final RecruitBoardService recruitBoardService;
    private final FreeBoardRepository freeBoardRepository;
    private final QnaBoardRepository qnaBoardRepository;
    private final RecruitBoardRepository recruitBoardRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final BoardRepository boardRepository;

    public List<AdminBoardDto> getBoardList(String board) {
        if(board.equals("free")){
            return freeBoardRepository.findAll()
                    .stream()
                    .map(AdminBoardDto::of)
                    .collect(Collectors.toList());
        }
        else if(board.equals("qna")){
            return qnaBoardRepository.findAll()
                    .stream()
                    .map(AdminBoardDto::of)
                    .collect(Collectors.toList());
        }else{
            return recruitBoardRepository.findAll()
                    .stream()
                    .map(AdminBoardDto::of)
                    .collect(Collectors.toList());
        }

    }

    public List<AdminUserDto> getUserList() {
        return userRepository.findAll()
                .stream()
                .map(AdminUserDto::of)
                .filter(user -> !user.getStuId().equals("ADMIN"))
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteBoard(String boardType, Long boardId) {
        if(boardType.equals("FreeBoard")){
            freeBoardService.delete(boardId);
        }else if(boardType.equals("QnaBoard")){
            qnaBoardService.delete(boardId);
        }else{
            recruitBoardService.delete(boardId);
        }
    }

    @Transactional
    public void deleteUser(Long userId) {
        boardRepository.deleteAllByUser(userRepository.findById(userId)
                .orElseThrow(()-> new IllegalArgumentException("해당 유저가 없습니다")));
        userRepository.deleteById(userId);
    }
}