package com.team3.devinit_back.board.entity;

import com.team3.devinit_back.common.BaseEntity;
import com.team3.devinit_back.member.entity.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
public class Board extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String content;

    private int upCnt;
    private int commentCnt;
    private int viewCnt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Recommendation> recommendationEntities = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TagBoard> tagBoards = new ArrayList<>();



    @Builder
    public Board(String id, String title, String content, Member member, int upCnt,
                  int commentCnt, int viewCnt, Category category){
        this.id = id;
        this.title = title;
        this.content = content;
        this.member = member;
        this.upCnt = upCnt;
        this.commentCnt = commentCnt;
        this.viewCnt = viewCnt;
        this.category = category;
    }
}
