package com.team3.devinit_back.member.entity;

import com.team3.devinit_back.board.entity.Board;
import com.team3.devinit_back.board.entity.Recommendation;
import com.team3.devinit_back.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Member extends BaseEntity {

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


    private String profileImage;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Board> boards = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Recommendation> recommendationEntities = new ArrayList<>();

}
