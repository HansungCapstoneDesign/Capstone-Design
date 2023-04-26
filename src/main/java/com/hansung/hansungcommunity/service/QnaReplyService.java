package com.hansung.hansungcommunity.service;

import com.hansung.hansungcommunity.dto.free.FreeReplyDto;
import com.hansung.hansungcommunity.dto.qna.QnaReplyDto;
import com.hansung.hansungcommunity.dto.user.UserReplyDto;
import com.hansung.hansungcommunity.entity.*;
import com.hansung.hansungcommunity.repository.QnaBoardRepository;
import com.hansung.hansungcommunity.repository.QnaReplyRepository;
import com.hansung.hansungcommunity.repository.UserRepository;
import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QnaReplyService {

    private final QnaReplyRepository replyRepository;
    private final QnaBoardRepository boardRepository;
    private final UserRepository userRepository;

    /**
     * 댓글 생성
     * DTO parentId 필드 null 여부로 부모, 자식 결정
     */
    @Transactional
    public QnaReplyDto create(Long userId, Long boardId, QnaReplyDto replyDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("댓글 생성 실패, 해당 유저가 없습니다."));

        QnaBoard board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("댓글 생성 실패, 해당 게시글이 없습니다."));

        QnaReply parent = null;

        if (replyDto.getParentId() != null) { // 자식 댓글인 경우
            parent = replyRepository.findById(replyDto.getParentId()) // 부모 댓글 지정
                    .orElseThrow(() -> new IllegalArgumentException("대댓글 생성 실패, 부모 댓글이 없습니다."));

            if (!parent.getBoard().getId().equals(boardId)) {
                throw new IllegalArgumentException("대댓글 생성 실패, 부모 댓글과 게시글이 다릅니다.");
            }
        }

        QnaReply qnaReply = QnaReply.of(user, board, replyDto); // 부모 댓글 없이 생성 후

        if (parent != null) { // 부모 댓글이 있다면
            qnaReply.updateParent(parent); // 부모 댓글 지정
        }

        QnaReply savedReply = replyRepository.save(qnaReply);

        if (parent != null) { // 부모 댓글이 있다면
            return QnaReplyDto.createChildren(savedReply); // 자식 댓글
        } else {
            return QnaReplyDto.createParent(savedReply); // 부모 댓글
        }

    }

    /**
     * 전체 댓글 조회
     * 게시글 id로 댓글 조회
     * QuerydslPredicateExecutor 활용 시, 한계점 존재
     * 추후 학습, 논의를 통해 구체화 필요
     */
    public List<QnaReplyDto> findAllByBoardId(Long boardId) {
        QQnaReply qnaReply = QQnaReply.qnaReply;
        Predicate predicate = qnaReply.board.id.eq(boardId);

        Iterable<QnaReply> replies = replyRepository.findAll(predicate);

        List<QnaReplyDto> result = new ArrayList<>();

        replies.forEach(reply -> { // Iterable to List
            if (reply.getParent() == null){ // 부모 댓글인 경우
                QnaReplyDto parent = QnaReplyDto.createParent(reply);
                result.add(parent);
            }
            else { // 자식 댓글인 경우
                QnaReplyDto children = QnaReplyDto.createChildren(reply);
                result.add(children);
            }
        });

        return result;
    }

    /**
     *채택 (해당 댓글 id로 채택값을 true로 바꾸고, 해당 댓글의 유저 포인트 증가)
     */
    @Transactional
    public Boolean adopt(Long replyId,int point,Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new IllegalArgumentException("해당 유저가 없습니다."));
        QnaReply reply = replyRepository.findById(replyId)
                .orElseThrow(()-> new IllegalArgumentException("해당 댓글이 없습니다."));
        reply.adopt(true);
        reply.getUser().setPlustPoint(point);
        if(user.getPoint()>=point) {
            user.setMinusPoint(point);
        }
        replyRepository.save(reply);
        return reply.getAdopt();
    }

    @Transactional(readOnly = true)
    public Boolean adoptCheck(Long boardId) {
        QnaReply reply = replyRepository.findById(boardId)
                .orElseThrow(()-> new IllegalArgumentException("해당 게시글이 없습니다."));

        return replyRepository.findAllByBoardId(reply.getId())
                .stream()
                .anyMatch(freeReply -> Boolean.TRUE.equals(reply.getAdopt()));
    }

    @Transactional
    public QnaReplyDto update(QnaReplyDto replyDto) {
        QnaReply reply = replyRepository.findById(replyDto.getId())
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글이 없습니다."));
        reply.update(replyDto.getArticle());
        UserReplyDto userReplyDto = new UserReplyDto(userRepository.findById(reply.getUser().getId())
                .orElseThrow(()-> new IllegalArgumentException("해당 유저가 없습니다.")));
        return QnaReplyDto.from(replyRepository.save(reply), userReplyDto);

    }
}
