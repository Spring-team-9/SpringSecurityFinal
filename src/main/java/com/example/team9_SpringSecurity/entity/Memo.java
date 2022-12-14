package com.example.team9_SpringSecurity.entity;

import com.example.team9_SpringSecurity.dto.MemoRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter                                                                         // Class 모든 필드의 Getter method를 생성
@Entity                                                                         // Entity임을 선언
@NoArgsConstructor                                                              // @NoArgsConstructor : 파라미터가 없는 기본 생성자를 생성
public class Memo extends Timestamped {

    @Id     // ID임을 선언
    @Column(name = "MEMO_ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long memoId;                                                        // 메모Id

    @Column(nullable = false)
    private String title;                                                       // 메모 제목

    @Column(nullable = false)
    private String username;                                                    // 메모 작성자 이름

    @Column(nullable = false)
    private String content;                                                     // 메모 내용

    @ManyToOne                                                                  // Memo(many) <-> User(one) Join
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "memo", cascade = CascadeType.REMOVE)          // Memo(1) <-> Replies(n) 관계, 상위 엔티티가 삭제될 경우 하위 엔티티도 삭제되도록 영속성 전이 처리
    @OrderBy("createdAt desc")                                           // 엔티티단에서 생성일자 기준으로 정렬
    private List<Reply> replies = new ArrayList<>();                     // n 부분을 List로 받기

    @OneToMany(mappedBy = "memo", cascade = CascadeType.REMOVE)          // Memo(1) <-> Like(n) 관계, 상위 엔티티가 삭제될 경우 하위 엔티티도 삭제되도록 영속성 전이 처리
    private List<LikeMemo> likes = new ArrayList<>();                    // n 부분을 List로 받기

    public Memo(MemoRequestDto dto, User user) {
        this.title = dto.getTitle();
        this.username = user.getUsername();
        this.content = dto.getContent();
        this.user = user;
    }

    public void update(MemoRequestDto dto) {
        this.title = dto.getTitle();
        this.content = dto.getContent();
    }


}
