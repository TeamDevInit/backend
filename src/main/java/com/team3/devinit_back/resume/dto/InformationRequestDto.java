package com.team3.devinit_back.resume.dto;

import com.team3.devinit_back.resume.entity.Resume;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class InformationRequestDto {
    @NotBlank(message = "이름을 입력해주세요")
    private String name;
    @NotBlank(message = "포지션을 입력해주세요")
    private String position;
    @NotBlank(message = "경력기간을 입력해주세요")
    private String employmentPeriod;
    private String summary;
    private String portfolio;
}
