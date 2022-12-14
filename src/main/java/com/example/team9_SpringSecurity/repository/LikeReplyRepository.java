package com.example.team9_SpringSecurity.repository;

import com.example.team9_SpringSecurity.entity.LikeReply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;

public interface LikeReplyRepository extends JpaRepository<LikeReply, Long>  {

    Optional<LikeReply> findAllByUserId(Long id);
    
    void deleteByMemoMemoId(Long id);

    // 좋아요 갯수 반환을 위한 Native-query
    // 서비스에서 대상 id값을 넣어서 호출하면 카운트값을 LONG형으로 반환함
    @Query(
            value = "select count(1) From like_reply AS a INNER JOIN reply AS b ON a.reply_id = b.reply_id WHERE a.reply_id = :id",
            nativeQuery = true)
    long totalCnt(Long id);

}
