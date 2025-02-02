package com.team3.devinit_back.resume.dto;

import com.team3.devinit_back.resume.entity.Resume;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@Getter
@Setter
@RequiredArgsConstructor
public class ResumeResponseDto {

    private String id;
    private String memberId;
    private InformationResponseDto information;
    private Set<ActivityResponseDto> activities = new LinkedHashSet<>();
    private Set<EducationResponseDto> educations = new LinkedHashSet<>();
    private Set<ExperienceResponseDto> experiences = new LinkedHashSet<>();
    private Set<LanguageResponseDto> languages = new LinkedHashSet<>();
    private Set<ProjectResponseDto> projects = new LinkedHashSet<>();
    private Set<SkillResponseDto> skills = new LinkedHashSet<>();
    public ResumeResponseDto(Resume resume){
        this.id = resume.getId();
        this.memberId = resume.getMember().getId();
        this.information = resume.getInformation() != null ? InformationResponseDto.fromEntity(resume.getInformation()) : null;
        this.skills.addAll(resume.getSkills().stream().map(SkillResponseDto::fromEntity).toList());
        this.activities.addAll(resume.getActivities().stream().map(ActivityResponseDto::fromEntity).toList());
        this.educations.addAll(resume.getEducations().stream().map(EducationResponseDto::fromEntity).toList());
        this.experiences.addAll(resume.getExperiences().stream().map(ExperienceResponseDto::fromEntity).toList());
        this.languages.addAll(resume.getLanguages().stream().map(LanguageResponseDto::fromEntity).toList());
        this.projects.addAll(resume.getProjects().stream().map(ProjectResponseDto::fromEntity).toList());

    }

    public static ResumeResponseDto fromEntity(Resume resume){ return new ResumeResponseDto(resume);}
}
