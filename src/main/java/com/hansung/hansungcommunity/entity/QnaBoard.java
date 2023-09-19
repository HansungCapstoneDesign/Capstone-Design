package com.hansung.hansungcommunity.entity;


import com.hansung.hansungcommunity.dto.qna.QnaBoardRequestDto;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import javax.persistence.*;

@Entity
@Getter
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QnaBoard extends Board {

    @Id
    private Long id;
    private String language;
    @OneToOne(mappedBy = "qnaBoard", cascade = CascadeType.REMOVE)
    @JoinColumn(name = "adopt_id")
    private Adopt adopt;

    private QnaBoard(User user, String title, String content, String language) {
        super.setUser(user);
        this.title = title;
        this.content = content;
        this.language = language;
    }

    private QnaBoard(String title, String content, String language) {
        this.title = title;
        this.content = content;
        this.language = language;
    }

    public static QnaBoard of(User user, String title, String content, String tag, String language) {
        return new QnaBoard(user, title, content, language);
    }

    public static QnaBoard of(String title, String content, String language) {
        return new QnaBoard(title, content, language);
    }

    public void updateBoard(QnaBoardRequestDto dto) {
        if (dto.getTitle() != null) this.title = dto.getTitle();
        if (dto.getContent() != null) this.content = dto.getContent();
        this.language = dto.getLanguage();
        modified();
    }

    // 조회수 증가 메소드
    public void increaseHits() {
        increaseViews();
    }
    public void setId(Long id){
        this.id = id;
    }


}
