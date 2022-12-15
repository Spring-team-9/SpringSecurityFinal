package com.example.team9_SpringSecurity.repository;

import com.example.team9_SpringSecurity.entity.LikeMemo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

public interface LikeMemoRepository extends JpaRepository<LikeMemo, Long> {

    Optional<LikeMemo> findAllByUserId(Long id);
    void deleteByUserId(Long id);

    // 좋아요 갯수 반환을 위한 Native-query
    // 서비스에서 대상 id값을 넣어서 호출하면 카운트값을 LONG형으로 반환함
    @Query(
            value = "select count(1) From like_memo AS a INNER JOIN memo AS b ON a.memo_id = b.memo_id WHERE a.memo_id = :id",
            nativeQuery = true)
    long totalCnt(@PathVariable("id") Long id);

}
