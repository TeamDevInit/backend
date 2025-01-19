package com.team3.devinit_back.board.entity;

import com.team3.devinit_back.comment.entity.Comment;
import com.team3.devinit_back.global.common.BaseEntity;
import com.team3.devinit_back.member.entity.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Board extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String content;
    private String thumbnail;

    private int upCnt;
    private int commentCnt;
    private int viewCnt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Recommendation> recommendation = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TagBoard> tagBoards = new ArrayList<>();

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comment  = new ArrayList<>();


    @Builder
    public Board(String title, String content, Member member, int upCnt,
                  int commentCnt, int viewCnt, Category category, String thumbnail){
        this.title = title;
        this.content = content;
        this.member = member;
        this.upCnt = upCnt;
        this.commentCnt = commentCnt;
        this.viewCnt = viewCnt;
        this.category = category;
        this.thumbnail = thumbnail;
    }
}
