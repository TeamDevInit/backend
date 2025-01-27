package com.team3.devinit_back.resume.dto;


import com.team3.devinit_back.resume.entity.Resume;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EducationRequestDto {
    private Long id;
    @NotBlank(message = "학교/기관명을 입력해주세요")
    private String organization;
    @NotBlank(message = "학위를 입력해주세요")
    private String degree;
    @NotBlank(message = "전공명을 입력해주세요")
    private String major;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String status;
}
