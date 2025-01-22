package com.team3.devinit_back.profile.dto;

import com.team3.devinit_back.follow.dto.FollowCountResponse;
import com.team3.devinit_back.profile.entity.Profile;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProfileDetailResponse {
    private String id;
    private String nickname;
    private String profileImage;
    private String about;
    private int boardCount;
    private int followerCount;
    private int followingCount;
    private boolean isFollowing;

    public static ProfileDetailResponse fromEntity(Profile profile, FollowCountResponse followCounts, boolean isFollowing) {
        return ProfileDetailResponse.builder()
            .id(profile.getId())
            .nickname(profile.getMember().getNickName())
            .about(profile.getAbout())
            .profileImage(profile.getMember().getProfileImage())
            .boardCount(profile.getBoardCount())
            .followerCount(followCounts.getFollowerCount())
            .followingCount(followCounts.getFollowingCount())
            .isFollowing(isFollowing)
            .build();
    }
}