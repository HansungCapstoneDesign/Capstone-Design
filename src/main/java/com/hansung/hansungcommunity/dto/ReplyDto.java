package com.hansung.hansungcommunity.dto;

import com.hansung.hansungcommunity.dto.user.UserReplyDto;
import com.hansung.hansungcommunity.entity.Reply;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReplyDto {
    private Long id;
    @NotEmpty
    private String article;
    private Long parentId;
    private UserReplyDto user;
    private LocalDateTime createdAt;

    private ReplyDto(Long id, String article, UserReplyDto dto, Long parentId, LocalDateTime createdAt) {
        this.id = id;
        this.article = article;
        this.user = dto;
        this.parentId = parentId;
        this.createdAt = createdAt;
    }


    public ReplyDto(Reply Reply) {
        this.id = Reply.getId();
        this.article = Reply.getArticle();
        this.parentId = (Reply.getParent() != null) ? Reply.getParent().getId() : null;
        this.user = new UserReplyDto(Reply.getUser());
        this.parentId = parentId;
        this.createdAt = Reply.getCreatedAt();

    }

    public ReplyDto(Long id, String article, UserReplyDto userReplyDto, LocalDateTime createdAt) {
        this.id = id;
        this.article = article;
        this.user = userReplyDto;
        this.createdAt = createdAt;
    }

    public static ReplyDto from(Reply Reply, UserReplyDto dto) {
        Long parentId = Reply.getParent() != null ? Reply.getParent().getId() : null;
        return new ReplyDto(
                Reply.getId(),
                Reply.getArticle(),
                dto,
                parentId,
                Reply.getCreatedAt()
        );
    }


    public static ReplyDto createParent(Reply Reply) {
        return new ReplyDto(
                Reply.getId(),
                Reply.getArticle(),
                new UserReplyDto(Reply.getUser()),
                Reply.getCreatedAt()
        );
    }

    public static ReplyDto createChildren(Reply Reply) {
        return new ReplyDto(
                Reply.getId(),
                Reply.getArticle(),
                Reply.getParent().getId(),
                new UserReplyDto(Reply.getUser()),
                Reply.getCreatedAt()
        );
    }

    public static List<ReplyDto> listOf(List<Reply> replies) {
        return replies.stream()
                .map(ReplyDto::new)
                .collect(Collectors.toList());
    }

}
