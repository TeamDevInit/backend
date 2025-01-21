package com.team3.devinit_back.resume.dto;

import com.team3.devinit_back.resume.entity.Project;
import com.team3.devinit_back.resume.entity.Resume;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;

@Data
@Getter
public class ProjectResponseDto {

    private Long id;
    private String projectName;
    private String description;
    private String organization;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    private String resumeId;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ProjectResponseDto(Project project){
        this.projectName = this.getProjectName();
        this.description = this.getDescription();
        this.organization = this.getOrganization();
        this.startDate = this.getStartDate();
        this.endDate = this.getEndDate();
        Resume resume = project.getResume();
        if(resume!=null){this.resumeId = resume.getId();}

        this.createdAt = project.getCreatedAt();
        this.updatedAt = project.getUpdatedAt();
    }
    public static ProjectResponseDto fromEntity(Project project){ return new ProjectResponseDto(project);}

}
