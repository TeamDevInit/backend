package com.team3.devinit_back.profile.dto;

import com.team3.devinit_back.resume.dto.ResumeDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProfileDto {
    private String nickname;
    private String about;
    private String profileImage;
}