package com.hansung.hansungcommunity.dto;

import lombok.Data;

/**
 * 구인 게시글 생성 요청 DTO
 * 추후 필드 추가 등 작업 요망
 */

@Data
public class RecruitBoardRequestDto {

    private String title;
    private String content;

}
