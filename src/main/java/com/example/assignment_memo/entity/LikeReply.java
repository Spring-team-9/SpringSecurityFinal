package com.example.assignment_memo.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
public class LikeReply {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "memo_id")
    private Memo memo;

    @ManyToOne
    @JoinColumn(name = "reply_id")
    private Reply reply;


    public LikeReply(User user, Memo memo, Reply reply){
        this.user = user;
        this.memo = memo;
        this.reply = reply;
    }
}
