package com.example.assignment_memo.repository;

import com.example.assignment_memo.entity.LikeReply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeReplyRepository extends JpaRepository<LikeReply, Long> {

    // 댓글 좋아요 카운트
    Optional<Long> countByReply_ReplyId(Long replyId);

    // 메모 ID와 댓글 ID, 유저 ID 일치 조건 select
    Optional<LikeReply> findByMemo_memoIdAndReply_ReplyIdAndUser_Id(Long memoId, Long replyId, Long userId);
}
