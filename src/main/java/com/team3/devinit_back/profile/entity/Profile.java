package com.team3.devinit_back.profile.entity;

import com.team3.devinit_back.member.entity.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "Profile")
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @OneToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne
    @JoinColumn(name = "grade_id", nullable = false)
    private Grade grade;

    @Column(name = "about", length = 255)
    private String about;

    @Column(name = "board_cnt", nullable = false)
    private int boardCount = 0;

    @Column(name = "follower_cnt", nullable = false)
    private int followerCount = 0;

    @Column(name = "following_cnt", nullable = false)
    private int followingCount = 0;
}
