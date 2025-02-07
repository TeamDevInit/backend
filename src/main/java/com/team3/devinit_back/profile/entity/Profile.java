package com.team3.devinit_back.profile.entity;

import com.team3.devinit_back.global.common.BaseEntity;
import com.team3.devinit_back.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Profile")
public class Profile extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "about", length = 255)
    private String about;

    @Column(name = "board_cnt", nullable = false)
    private int boardCount = 0;

    @Column(name = "follower_cnt", nullable = false)
    private int followerCount = 0;

    @Column(name = "following_cnt", nullable = false)
    private int followingCount = 0;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Builder
    public Profile(String about, int boardCount, int followerCount, int followingCount, Member member) {
        this.about = about;
        this.boardCount = boardCount;
        this.followerCount = followerCount;
        this.followingCount = followingCount;
        this.member = member;
    }

    public void update(String about) {
        this.about = about;
    }

    public void incrementFollowerCount() {
        this.followerCount++;
    }
    public void decrementFollowerCount() {
        if (this.followerCount > 0) {
            this.followerCount--;
        }
    }

    public void incrementFollowingCount() {
        this.followingCount++;
    }

    public void decrementFollowingCount() {
        if (this.followingCount > 0) {
            this.followingCount--;
        }
    }

    public void incrementBoardCount() {
        this.boardCount++;
    }
}