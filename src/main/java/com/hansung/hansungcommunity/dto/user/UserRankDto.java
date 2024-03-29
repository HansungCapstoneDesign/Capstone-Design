package com.hansung.hansungcommunity.dto.user;

import com.hansung.hansungcommunity.entity.User;
import lombok.Getter;

@Getter
public class UserRankDto {

    private final int adoptSize;
    private final String nickname;
    private final String studentId;
    private final String introduce;
    private final String profileImg;

    private UserRankDto(int adoptSize, String nickname, String studentId, String introduce, String profileImg) {
        this.adoptSize = adoptSize;
        this.nickname = nickname;
        this.studentId = studentId;
        this.introduce = introduce;
        this.profileImg = profileImg;
    }

    public static UserRankDto of(Object[] obj) {
        return new UserRankDto(
                ((Long) obj[1]).intValue(),
                ((User) obj[0]).getNickname(),
                ((User) obj[0]).getStudentId(),
                ((User) obj[0]).getIntroduce(),
                ((User) obj[0]).getProfileImg()
        );
    }

}
