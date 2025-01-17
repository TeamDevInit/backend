package com.team3.devinit_back.comment.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.team3.devinit_back.board.entity.Board;
import com.team3.devinit_back.global.common.BaseEntity;
import com.team3.devinit_back.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    @JsonIgnore
    private Comment parentComment;

    private int commentCnt;

    @OneToMany(mappedBy = "parentComment", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Comment> children = new ArrayList<>(); // 대댓글 리스트

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;


    @Builder
    public Comment(String content, Member member, Board board, Comment parentComment, int commentCnt){
        this.content = content;
        this.member = member;
        this.board = board;
        this.parentComment = parentComment;
        this.commentCnt = commentCnt;

    }

}
