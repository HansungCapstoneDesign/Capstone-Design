package com.hansung.hansungcommunity.repository;

import com.hansung.hansungcommunity.entity.FreeReply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FreeReplyRepository extends JpaRepository<FreeReply, Long>,
        QuerydslPredicateExecutor<FreeReply> {


    @Query("SELECT DISTINCT r FROM FreeReply r LEFT JOIN FETCH r.children WHERE r.freeBoard.id = :articleId ORDER BY r.id ASC")
    List<FreeReply> findAllWithChildrenByArticleId(@Param("articleId") Long articleId);

    Optional<List<FreeReply>> findAllByUserId(Long userId);
    Optional<FreeReply> findByFreeBoardId(Long userId);


}
