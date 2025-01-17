package com.team3.devinit_back.follow.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FollowCountResponse {
    private int followerCount;
    private int followingCount;
}
