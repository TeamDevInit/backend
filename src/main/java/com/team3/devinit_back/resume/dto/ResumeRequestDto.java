package com.team3.devinit_back.resume.dto;

import lombok.Data;

import java.util.LinkedHashSet;
import java.util.Set;

@Data
public class ResumeRequestDto {
    private String id;
    private InformationResponseDto information;
    private Set<ActivityResponseDto> activities = new LinkedHashSet<>();
    private Set<EducationResponseDto> educations = new LinkedHashSet<>();
    private Set<ExperienceResponseDto> experiences = new LinkedHashSet<>();
    private Set<LanguageResponseDto> languages = new LinkedHashSet<>();
    private Set<ProjectResponseDto> projects = new LinkedHashSet<>();
}
