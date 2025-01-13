package com.team3.devinit_back.profile.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileDetailDto {
    private String nickname;
    private String profileImage;
    private String about;
    private int boardCount;
    private int followerCount;
    private int followingCount;
}
