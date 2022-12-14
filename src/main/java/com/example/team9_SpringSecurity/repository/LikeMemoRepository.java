package com.example.team9_SpringSecurity.repository;

import com.example.team9_SpringSecurity.entity.LikeMemo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeMemoRepository extends JpaRepository<LikeMemo, Long> {

    // 댓글 좋아요 카운트
    Optional<Long> countByMemo_MemoId(Long memoId);

    // 메모 ID와 유저 ID 일치 조건 select
    Optional<LikeMemo> findByMemo_MemoIdAndUser_Id(Long memoId, Long userId);
}
