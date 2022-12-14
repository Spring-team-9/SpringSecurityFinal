package com.example.team9_SpringSecurity.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity(name = "users")
@Getter
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)                                // Enum을 사용하며 어떤 방식으로 사용할지 설정 // 해당 문자열 그대로 사용
    private UserRoleEnum role;
                                                                        // 회원 탈퇴시 데이터 삭제 처리를 위한 연관관계 설정
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)         // User(1) <-> memo(n) 관계, 상위 엔티티가 삭제될 경우 하위 엔티티도 삭제되도록 영속성 전이 처리
    private List<Memo> memo;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)         // User(1) <-> Reply(n) 관계, 상위 엔티티가 삭제될 경우 하위 엔티티도 삭제되도록 영속성 전이 처리
    private List<Reply> reply;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)         // User(1) <-> likememo(n) 관계, 상위 엔티티가 삭제될 경우 하위 엔티티도 삭제되도록 영속성 전이 처리
    private List<LikeMemo> likeMemos;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)         // User(1) <-> likeReply(n) 관계, 상위 엔티티가 삭제될 경우 하위 엔티티도 삭제되도록 영속성 전이 처리
    private List<LikeReply> likeReplies;

    public User(String username, String password, UserRoleEnum role){
        this.username = username;
        this.password = password;
        this.role = role;
    }
}
