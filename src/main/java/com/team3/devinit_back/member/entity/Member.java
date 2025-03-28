package com.team3.devinit_back.member.entity;

import com.team3.devinit_back.board.entity.Board;
import com.team3.devinit_back.board.entity.Recommendation;
import com.team3.devinit_back.global.common.BaseEntity;
import com.team3.devinit_back.profile.entity.Profile;
import com.team3.devinit_back.resume.entity.Resume;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, unique = true)
    private String nickName;

    @Column(nullable = false, unique = true)
    private String socialId;

    @Column(nullable = false)
    private String socialProvider;

    @Column(nullable = false)
    private String role;

    private String profileImage;

    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private Profile profile;

    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private Resume resume;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Board> boards = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Recommendation> recommendation = new ArrayList<>();

    public void updateProfile(String nickName, String profileImage) {
        this.nickName = nickName;
        this.profileImage = profileImage;
    }

    @Builder
    public Member(String nickName, String socialId, String socialProvider,String role, String profileImage){
        this.nickName = nickName;
        this.socialId = socialId;
        this.profileImage = profileImage;
        this.role = role;
        this.socialProvider = socialProvider;
    }
}
