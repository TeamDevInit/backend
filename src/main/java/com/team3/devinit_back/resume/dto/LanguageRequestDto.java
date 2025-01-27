package com.team3.devinit_back.resume.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LanguageRequestDto {
    private Long id;
    @NotBlank(message = "어학명을 입력해주세요")
    private String name;
    @NotBlank(message = "어학성적을 입력해주세요")
    private String level;
}
