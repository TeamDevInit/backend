package com.team3.devinit_back.profile.dto;

import com.team3.devinit_back.follow.dto.FollowCountResponse;
import com.team3.devinit_back.profile.entity.Profile;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfileDetailResponse {
    private String id;
    private String nickname;
    private String profileImage;
    private String about;
    private int boardCount;
    private int followerCount;
    private int followingCount;

    public ProfileDetailResponse(Profile profile, FollowCountResponse followCounts) {
        this.id = profile.getId();
        this.nickname = profile.getMember().getNickName();
        this.profileImage = profile.getMember().getProfileImage();
        this.about = profile.getAbout();
        this.boardCount = profile.getBoardCount();
        this.followerCount = followCounts.getFollowerCount();
        this.followingCount = followCounts.getFollowingCount();
    }

    public static ProfileDetailResponse fromEntity(Profile profile, FollowCountResponse followCounts) {
        return new ProfileDetailResponse(profile, followCounts);
    }
}