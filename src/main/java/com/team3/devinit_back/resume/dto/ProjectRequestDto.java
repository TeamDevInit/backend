package com.team3.devinit_back.resume.dto;

import lombok.Data;

import java.time.LocalDateTime;
@Data
public class ProjectRequestDto {
    private String projectName;
    private String description;
    private String organization;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
