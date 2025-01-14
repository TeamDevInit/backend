package com.team3.devinit_back.member.entity;

import com.team3.devinit_back.board.entity.BoardEntity;
import com.team3.devinit_back.board.entity.RecommendationEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class MemberEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    //private String name;

    @Column(nullable = false, unique = true)
    private String nickName;

    @Column(nullable = false, unique = true)
    private String socialId;

    @Column(nullable = false)
    private String socialProvider;

    @Column(nullable = false)
    private String role;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BoardEntity> boards = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RecommendationEntity> recommendationEntities = new ArrayList<>();

}
