package com.hansung.hansungcommunity.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.auth.FirebaseAuthException;
import com.hansung.hansungcommunity.auth.CustomAuthentication;
import com.hansung.hansungcommunity.dto.free.*;
import com.hansung.hansungcommunity.dto.media.FileDto;
import com.hansung.hansungcommunity.dto.media.FileRequestDto;
import com.hansung.hansungcommunity.entity.FreeBoard;
import com.hansung.hansungcommunity.service.FileService;
import com.hansung.hansungcommunity.service.FireBaseService;
import com.hansung.hansungcommunity.service.FreeBoardService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Random;

@RestController
@RequiredArgsConstructor // 생성자 주입 (final 키워드)
@RequestMapping("/api")
public class FreeBoardApiController {

    private final FreeBoardService freeBoardService;
    private final FileService fileService;
    private final FireBaseService fireBaseService;

    /**
     * 모든 게시글 조회 (게시글 4개반환)
     */
    @GetMapping("/free/main")
    public ResponseEntity<Result<List<FreeBoardMainDto>>> getList() {
        List<FreeBoardMainDto> dtoList = freeBoardService.findAll();

        return ResponseEntity.status(HttpStatus.OK).body(new Result<>(dtoList));
    }

    /**
     * 특정 게시글 조회
     * 조회수 증가 로직 구현을 위해 임의로 구현, 추후 수정
     */
    @GetMapping("/free/detail/{boardId}")
    public ResponseEntity<Result<FreeBoardDetailsDto>> getDetailedPost(
            @PathVariable("boardId") Long boardId,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        increaseHits(boardId, request, response);
        FreeBoardDetailsDto boardDto = freeBoardService.findOne(boardId);

        return ResponseEntity.status(HttpStatus.OK).body(new Result<>(boardDto));
    }

    /**
     * 게시글 수정 시, 게시글 상세 정보 조회
     */
    @GetMapping("/free/update/{boardId}")
    public ResponseEntity<FreeBoardUpdateDto> getUpdatingData(@PathVariable("boardId") Long boardId) {
        FreeBoardUpdateDto boardDto = freeBoardService.getUpdatingData(boardId);

        return ResponseEntity.status(HttpStatus.OK).body(boardDto);
    }

    /**
     * 자유 게시판 목록 페이지 (해당 페이지에 개수에 맞게 데이터 반환)
     * 페이지 정보는 프론트에서 전송
     */
    @GetMapping("/free/list")
    public ResponseEntity<ListResult<List<FreeBoardListDto>>> getListOfPage(Pageable pageable, @RequestParam(required = false) String search) {
        List<FreeBoardListDto> dtoList = freeBoardService.findByPage(pageable, search);
        long count = freeBoardService.getCount(search);

        return ResponseEntity.status(HttpStatus.OK).body(new ListResult<>(dtoList, count));
    }

    /**
     * 게시글 저장
     */
    @PostMapping("/free/no-file")
    public ResponseEntity<Long> createNonFilePost(
            @Valid @RequestBody FreeBoardRequestDto dto,
            Authentication authentication
    ) {
        CustomAuthentication ca = (CustomAuthentication) authentication;
        Long savedId = freeBoardService.createPost(ca.getUser().getId(), dto);

        // Wrapper 클래스로 감싼 후,
        // ResponseEntity 의 body 에 담아 반환
        return ResponseEntity.status(HttpStatus.OK).body(savedId);
    }

    /**
     * 게시글 저장 (업로드 파일 있을 때)
     */
    @PostMapping("/free")
    public ResponseEntity<Long> createPostWithFile(
            @RequestParam("file") MultipartFile[] file,
            String stringFree,
            Authentication authentication
    ) throws IOException, FirebaseAuthException {
        CustomAuthentication ca = (CustomAuthentication) authentication;
        FreeBoard freeBoard = new ObjectMapper().readValue(stringFree, FreeBoard.class);
        Long boardId = freeBoardService.getMappingUser(ca.getUser().getId(), freeBoard);

        for (MultipartFile f : file) {
            String fileName = f.getOriginalFilename();
            assert fileName != null;
            String extension = fileName.substring(fileName.lastIndexOf(".") + 1);
            String createdName = String.valueOf(createFilename());
            String name = createdName + "." + extension;
            FileDto dto = FileDto.of(freeBoard, fileName, name);
            fileService.saveFile(dto);
            fireBaseService.uploadFiles(f, name);

        }

        return ResponseEntity.status(HttpStatus.OK).body(boardId);
    }

    /**
     * 해당 자유게시판 게시글에서 첨부 파일이 있는지 체크
     */
    @GetMapping("/free/{boardId}/file-check")
    public ResponseEntity<Boolean> checkFile(@PathVariable("boardId") Long boardId) {
        boolean check = fileService.checkFileOfPost(boardId);
        return ResponseEntity.status(HttpStatus.OK).body(check);
    }

    /**
     * 해당 게시글에 첨부파일이 있음을 check 후, 해당 파일의 이름들 전송
     */
    @GetMapping("/free/{boardId}/file-list")
    public ResponseEntity<List<FileRequestDto>> getFileList(@PathVariable("boardId") Long boardId) {
        List<FileRequestDto> dtos = fileService.getListOfFile(boardId);
        return ResponseEntity.status(HttpStatus.OK).body(dtos);
    }

    /**
     * 게시글 수정
     */
    @PutMapping("/free/update/{boardId}")
    public ResponseEntity<Result<FreeBoardRequestDto>> updateNonFilePost(
            @PathVariable("boardId") Long boardId,
            @Valid @RequestBody FreeBoardRequestDto dto
    ) {
        FreeBoardRequestDto boardDto = freeBoardService.updatePost(boardId, dto);

        return ResponseEntity.status(HttpStatus.OK).body(new Result<>(boardDto));
    }

    /**
     * 게시글 수정 (첨부파일 있을 때)
     */
    @PutMapping("/free/update/{boardId}/file")
    public ResponseEntity<Result<FreeBoardRequestDto>> updatePostWithFile(
            @PathVariable("boardId") Long boardId,
            @RequestParam("file") MultipartFile[] file,
            String stringFree
    ) throws IOException, FirebaseAuthException {
        FreeBoard board = new ObjectMapper().readValue(stringFree, FreeBoard.class);
        FreeBoard real = freeBoardService.getDetailedPost(boardId);
        board.setId(real.getId());

        FreeBoardRequestDto boardDto = freeBoardService.updatePost(boardId, FreeBoardRequestDto.from(board));

        for (MultipartFile f : file) {
            String fileName = f.getOriginalFilename();
            assert fileName != null;
            String extension = fileName.substring(fileName.lastIndexOf(".") + 1);
            String createdName = String.valueOf(createFilename());
            String name = createdName + "." + extension;
            FileDto dto = FileDto.of(real, fileName, name);
            fileService.saveFile(dto);
            fireBaseService.uploadFiles(f, name);

        }
        return ResponseEntity.status(HttpStatus.OK).body(new Result<>(boardDto));

    }


    /**
     * 게시글 삭제
     */
    @DeleteMapping("/free/delete/{boardId}")
    public ResponseEntity<Void> deletePost(@PathVariable("boardId") Long boardId) {
        freeBoardService.deletePost(boardId);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * 조회수 증가 로직
     */
    private void increaseHits(Long boardId, HttpServletRequest request, HttpServletResponse response) {
        Cookie oldCookie = null;

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("freeBoardHits")) {
                    oldCookie = cookie;
                }
            }
        }

        if (oldCookie != null) {
            if (!oldCookie.getValue().contains("[" + boardId.toString() + "]")) {
                freeBoardService.increaseHits(boardId);
                oldCookie.setValue(oldCookie.getValue() + "_[" + boardId + "]");
                oldCookie.setPath("/");
                oldCookie.setMaxAge(60 * 60 * 24);
                response.addCookie(oldCookie);
            }
        } else {
            freeBoardService.increaseHits(boardId);
            Cookie newCookie = new Cookie("freeBoardHits", "[" + boardId + "]");
            newCookie.setPath("/");
            newCookie.setMaxAge(60 * 60 * 24);
            response.addCookie(newCookie);
        }
    }

    /**
     * 파일 이름 생성
     */
    public int createFilename() {
        Random random = new Random();
        return random.nextInt(1000);
    }


    // 확장성을 위한 Wrapper 클래스
    @Data
    @AllArgsConstructor
    static class Result<T> {
        private T data;
    }

    @Data
    @AllArgsConstructor
    static class ListResult<T> {
        private T data;
        private long count;
    }

}
