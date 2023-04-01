package com.hansung.hansungcommunity.dto;

import com.hansung.hansungcommunity.entity.FreeBoard;
import com.hansung.hansungcommunity.entity.User;
import lombok.Data;

import java.util.List;

@Data
public class UserRequestDto {
    private Long id;

    private String studentId;
    private String name; // 이름
    private String nickname; // 닉네임
    private String introduce; // 소개글
    private String track1;
    private String track2;


    public UserRequestDto() {}
    public UserRequestDto(Long id ,String studentId, String name, String nickname, String introduce, String track1, String track2) {
        this.id = id;
        this.studentId = studentId;
        this.nickname = nickname;
        this.name = name;
        this.introduce = introduce;
        this.track1 = track1;
        this.track2 = track2;
    }

    public static UserRequestDto from(User user){
        return new UserRequestDto(
                user.getId(),
                user.getStudentId(),
                user.getName(),
                user.getNickname(),
                user.getIntroduce(),
                user.getTrack1(),
                user.getTrack2()
        );
    }



}
