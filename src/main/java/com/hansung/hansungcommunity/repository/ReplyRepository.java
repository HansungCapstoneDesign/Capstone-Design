package com.hansung.hansungcommunity.repository;

import com.hansung.hansungcommunity.entity.Reply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReplyRepository extends JpaRepository<Reply, Long> {

    @Query("SELECT DISTINCT r FROM Reply r LEFT JOIN FETCH r.children WHERE r.board.id = :articleId ORDER BY r.id ASC")
    List<Reply> findAllWithChildrenByArticleId(@Param("articleId") Long articleId);

    List<Reply> findAllByParentId(Long id);

    Optional<Reply> findByBoardIdAndUserId(Long boardId, Long id);
}
