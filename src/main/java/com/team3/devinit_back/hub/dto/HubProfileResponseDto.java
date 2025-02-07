package com.team3.devinit_back.hub.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class HubProfileResponseDto {
    private String profileId;
    private String nickname;
    private String about;
    private String profileImage;
    private String employmentPeriod;
    private List<String> skills;
}