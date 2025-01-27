package com.team3.devinit_back.resume.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ExperienceRequestDto {
    private Long id;
    @NotBlank(message =  "회사명을 입력해주세요")
    private String companyName;
    @NotBlank(message =  "고용형태를 입력해주세요")
    private String employmentType;
    @NotBlank(message =  "상세설명을 입력해주세요")
    private String description;
    private String position;
    private String department;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

}
