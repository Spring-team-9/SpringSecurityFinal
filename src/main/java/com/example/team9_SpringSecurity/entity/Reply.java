package com.example.team9_SpringSecurity.entity;

import com.example.team9_SpringSecurity.dto.ReplyRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
public class Reply extends Timestamped{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)

    private Long replyId;                                           // 댓글 Id

    @ManyToOne                                                      // Reply(many) <-> Memo(one) Join
    @JoinColumn(name = "MEMO_ID", nullable = false)
    private Memo memo;

    @ManyToOne                                                      // Reply(many) <-> User(one) Join

    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private String replyName;                                       // 댓글 작성자 이름

    @Column(nullable = false)
    private String replyContent;                                    // 댓글 내용

    @OneToMany(mappedBy = "reply", cascade = CascadeType.REMOVE)    // Reply(1) <-> likeReply(n) 관계, 상위 엔티티가 삭제될 경우 하위 엔티티도 삭제되도록 영속성 전이 처리
    private List<LikeReply> likereply = new ArrayList<>();


    public Reply(ReplyRequestDto dto, User user, Memo memo){
        this.memo = memo;
        this.replyName = user.getUsername();
        this.replyContent = dto.getReplyContent();
        this.user = user;
    }

    public void update(ReplyRequestDto dto){
        this.replyContent = dto.getReplyContent();
    }
}
