package com.hansung.hansungcommunity.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hansung.hansungcommunity.entity.Summary;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserSummaryDto {

    private Long summaryId;
    @Size(min = 1, max = 100)
    private String content;
    private String language;
    @JsonProperty("isFixed")
    private boolean isFixed;
    private LocalDateTime date;

    private UserSummaryDto(Long summaryId, String content, LocalDateTime date, boolean isFixed, String language) {
        this.summaryId = summaryId;
        this.content = content;
        this.date = date;
        this.isFixed = isFixed;
        this.language = language;
    }

    public static UserSummaryDto of(Summary summary) {
        return new UserSummaryDto(
                summary.getId(),
                summary.getContent(),
                summary.getCreatedAt(),
                summary.isFixed(),
                summary.getLanguage()
        );
    }

}
