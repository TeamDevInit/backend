package com.team3.devinit_back.profile.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProfileDto {
    private String nickname;
    private String about;
    private String profileImage;
}