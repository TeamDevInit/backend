package com.team3.devinit_back.resume.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;
@Data
public class ActivityRequestDto {
    @NotBlank(message =  "활동명을 입력해주세요")
    private String activityName;
    @NotBlank(message =  "학교/기관명을 입력해주세요")
    private String organization;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String description;

}
