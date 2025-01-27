package com.team3.devinit_back.resume.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;
@Data
public class ProjectRequestDto {
    private Long id;
    @NotBlank(message = "프로젝트명을 입력해주세요")
    private String projectName;
    @NotBlank(message = "프로젝트 설명을 입력해주세요")
    private String description;
    @NotBlank(message = "조직/기관명을 입력해주세요")
    private String organization;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
