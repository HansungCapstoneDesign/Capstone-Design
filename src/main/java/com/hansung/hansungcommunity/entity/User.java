package com.hansung.hansungcommunity.entity;


import com.hansung.hansungcommunity.dto.UserRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@Entity
@Table(name = "user")
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stu_id")
    private Long id;

    private String studentId;
    private String name; // 이름
    private int point; // 포인트
    private String nickname; // 닉네임
    private String career; // 경력
    private String introduce; // 소개글
    private String track1;
    private String track2;

    // private String todayComment; // 오늘의 한마디 (테이블 분리 ?)
    // private Image profileImage // 프로필 사진, 추후 개발
    // private String skillStack; // 기술 스택, 추후 개발
    @OrderBy("createdAt DESC")
    @OneToMany(mappedBy = "user")
    private List<FreeBoard> postFreeBoards = new ArrayList<>();

    @OrderBy("createdAt DESC")
    @OneToMany(mappedBy = "user")
    private List<QnaBoard> postQnaBoard = new ArrayList<>();

    public User(Long id,String studentId, String name, String nickname, String introduce, String track1, String track2) {
        this.id = id;
        this.studentId = studentId;
        this.name = name;
        this.nickname = nickname;
        this.introduce = introduce;
        this.track1 = track1;
        this.track2 = track2;
    }

    public static User of(UserRequestDto dto){
        return new User(
                dto.getId(),
                dto.getStudentId(),
                dto.getName(),
                dto.getNickname(),
                dto.getIntroduce(),
                dto.getTrack1(),
                dto.getTrack2()
        );
    }





}
