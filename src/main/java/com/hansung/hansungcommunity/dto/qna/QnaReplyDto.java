package com.hansung.hansungcommunity.dto.qna;

import com.hansung.hansungcommunity.dto.user.UserReplyDto;
import com.hansung.hansungcommunity.entity.QnaReply;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

/**
 * Q&A 게시글 댓글 DTO
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class QnaReplyDto {

    private Long id;
    @NotEmpty
    private String article;
    private Long parentId;
    private UserReplyDto user;
    private LocalDateTime createdAt;

    private QnaReplyDto(Long id, String article, Long parentId, UserReplyDto dto, LocalDateTime createdAt) {
        this.id = id;
        this.article = article;
        this.parentId = parentId;
        this.user = dto;
        this.createdAt = createdAt;
    }

    private QnaReplyDto(Long id, String article, UserReplyDto userReplyDto, LocalDateTime createdAt) {
        this.id = id;
        this.article = article;
        this.user = userReplyDto;
        this.createdAt = createdAt;
    }

    public static QnaReplyDto of(QnaReply qnaReply, UserReplyDto userDto) {
        Long parentId = qnaReply.getParent() != null ? qnaReply.getParent().getId() : null;
        return new QnaReplyDto(
                qnaReply.getId(),
                qnaReply.getArticle(),
                parentId,
                userDto,
                qnaReply.getCreatedAt()
        );
    }

    // 부모 댓글 DTO 생성
    public static QnaReplyDto createParent(QnaReply qnaReply) {
        return new QnaReplyDto(
                qnaReply.getId(),
                qnaReply.getArticle(),
                UserReplyDto.from(qnaReply.getUser()),
                qnaReply.getCreatedAt()
        );
    }

    // 자식 댓글 DTO 생성
    public static QnaReplyDto createChildren(QnaReply qnaReply) {
        return new QnaReplyDto(
                qnaReply.getId(),
                qnaReply.getArticle(),
                qnaReply.getParent().getId(),
                UserReplyDto.from(qnaReply.getUser()),
                qnaReply.getCreatedAt()
        );
    }

}