package com.hansung.hansungcommunity.dto;

import com.hansung.hansungcommunity.entity.QnaReply;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * Q&A 게시글 댓글 DTO
 * 추후 ResponseDto, RequestDto 분리
 * 추후 userId 등 고려 필요
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QnaReplyDto {
    private Long id;
    @NotNull
    private String article;

    private Long parentId;

    private QnaReplyDto(Long id, String article) {
        this.id = id;
        this.article = article;
    }


    // 부모 댓글 DTO 생성
    public static QnaReplyDto createParent(QnaReply qnaReply) {
        return new QnaReplyDto(
                qnaReply.getId(),
                qnaReply.getArticle()
        );
    }
    
    // 자식 댓글 DTO 생성
    public static QnaReplyDto createChildren(QnaReply qnaReply) {
        return new QnaReplyDto(
                qnaReply.getId(),
                qnaReply.getArticle(),
                qnaReply.getParent().getId()
        );
    }

}
