package com.hansung.hansungcommunity.repository;

import com.hansung.hansungcommunity.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>,
        QuerydslPredicateExecutor<User> {

    boolean existsUserByStudentId(String studentId);

    boolean existsUserByNickname(String nickname);

    Optional<User> findByStudentId(String studentId);

    User findByNickname(String nickname);

}
