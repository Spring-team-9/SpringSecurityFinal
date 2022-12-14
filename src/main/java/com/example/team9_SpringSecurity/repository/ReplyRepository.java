package com.example.team9_SpringSecurity.repository;


import com.example.team9_SpringSecurity.entity.Memo;
import com.example.team9_SpringSecurity.entity.Reply;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ReplyRepository extends JpaRepository<Reply,Long> {
    Optional<Reply> findByMemo_MemoIdAndReplyId(Long id, Long replyId);

    Long findByMemoMemoId(Long id);
    void deleteByReplyId(Long replyId);
    List<Reply> findAllByMemo(Memo memo);
}
