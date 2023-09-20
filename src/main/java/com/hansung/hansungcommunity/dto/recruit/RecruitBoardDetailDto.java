package com.hansung.hansungcommunity.dto.recruit;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hansung.hansungcommunity.entity.RecruitBoard;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
public class RecruitBoardDetailDto {

    private Long id;
    private String title;
    private String content;
    private String writer;
    private String introduce;
    private String profileImg;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private int bookmark;
    private int reply;
    private int views;
    private Long stuId;
    private String require;
    private String optional;
    private int party;
    private int gathered;
    @JsonProperty("isCompleted")
    private boolean isCompleted;

    private RecruitBoardDetailDto(Long id, String title, String content, String writer, String introduce, String profileImg, LocalDateTime createdDate, LocalDateTime modifiedDate, int bookmark, int reply, int views, Long stuId, String require, String optional, int party, boolean isCompleted) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.writer = writer;
        this.introduce = introduce;
        this.profileImg = profileImg;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
        this.bookmark = bookmark;
        this.reply = reply;
        this.views = views;
        this.stuId = stuId;
        this.require = require;
        this.optional = optional;
        this.party = party;
        this.isCompleted = isCompleted;
    }

    public static RecruitBoardDetailDto from(RecruitBoard recruitBoard) {
        return new RecruitBoardDetailDto(
                recruitBoard.getId(),
                recruitBoard.getTitle(),
                recruitBoard.getContent(),
                recruitBoard.getUser().getNickname(),
                recruitBoard.getUser().getIntroduce(),
                recruitBoard.getUser().getProfileImg(),
                recruitBoard.getCreatedAt(),
                recruitBoard.getModifiedAt(),
                recruitBoard.getBookmarks().size(),
                recruitBoard.getReplies().size(),
                recruitBoard.getViews(),
                Long.parseLong(recruitBoard.getUser().getStudentId()),
                recruitBoard.getRequired(),
                recruitBoard.getOptional(),
                recruitBoard.getParty(),
                recruitBoard.isCompleted()
        );
    }

    public void setGathered(int gathered) {
        this.gathered = gathered;
    }

}
