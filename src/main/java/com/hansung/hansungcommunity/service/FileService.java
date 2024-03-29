package com.hansung.hansungcommunity.service;


import com.hansung.hansungcommunity.dto.media.FileDto;
import com.hansung.hansungcommunity.dto.media.FileRequestDto;
import com.hansung.hansungcommunity.entity.FileEntity;
import com.hansung.hansungcommunity.exception.BoardNotFoundException;
import com.hansung.hansungcommunity.repository.BoardRepository;
import com.hansung.hansungcommunity.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class FileService {

    private final FileRepository fileRepository;
    private final BoardRepository boardRepository;

    @Transactional
    public FileDto saveFile(FileDto dto) {
        FileEntity fileEntity = FileEntity.of(dto.getBoard(), dto.getOriginalName(), dto.getCreatedName());
        FileEntity res = fileRepository.save(fileEntity);

        return FileDto.from(res);
    }

    public boolean checkFileOfPost(Long boardId) {
        return fileRepository.findAllByBoard(boardRepository.findById(boardId)
                        .orElseThrow(() -> new BoardNotFoundException("해당 게시글이 없습니다.")))
                .stream()
                .anyMatch(Objects::nonNull);
    }

    public List<FileRequestDto> getListOfFile(Long boardId) {
        return fileRepository.findAllByBoard(boardRepository.findById(boardId)
                        .orElseThrow(() -> new BoardNotFoundException("해당 게시글이 없습니다.")))
                .stream()
                .map(FileRequestDto::from)
                .collect(Collectors.toList());
    }

    public String getCreatedName(String imageName) {
        FileEntity fileEntity = fileRepository.findByOriginalName(imageName);
        return fileEntity.getCreatedName();
    }


    @Transactional
    public void deleteFile(String imageName) {
        fileRepository.deleteByCreatedName(imageName);
    }
}
