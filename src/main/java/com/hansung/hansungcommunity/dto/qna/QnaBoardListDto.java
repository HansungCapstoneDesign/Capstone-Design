package com.hansung.hansungcommunity.dto.qna;

import com.hansung.hansungcommunity.entity.QnaBoard;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@NoArgsConstructor
public class QnaBoardListDto {

    private Long id;
    private String title;
    private String content;
    private String writer;
    //TODO : 파일
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private String language;
    private int bookmark;
    private int reply;
    private int point;

    public QnaBoardListDto(QnaBoard board){
        this.id = board.getId();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.writer = board.getUser().getNickname();
        this.createdDate = board.getCreatedAt();
        this.modifiedDate = board.getModifiedAt();
        this.bookmark = board.getBookmarks();
        this.reply = board.getReplies().size();
        this.point = board.getPoint();
    }

}