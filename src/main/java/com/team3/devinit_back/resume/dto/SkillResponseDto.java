package com.team3.devinit_back.resume.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SkillResponseDto {
    private Long id;
    private String resumeId;
    private List<String> skillTagNames;
}