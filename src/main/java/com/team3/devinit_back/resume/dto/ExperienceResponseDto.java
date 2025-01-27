package com.team3.devinit_back.resume.dto;

import com.team3.devinit_back.resume.entity.Experience;
import com.team3.devinit_back.resume.entity.Resume;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;

@Data
@Getter
public class ExperienceResponseDto {

    private Long id;
    private String companyName;
    private String employmentType;
    private String description;
    private String position;
    private String department;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String resumeId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ExperienceResponseDto(Experience experience){
        this.companyName = experience.getCompanyName();
        this.employmentType = experience.getEmploymentType();
        this.description = experience.getDescription();
        this.position = experience.getPosition();
        this.department = experience.getDepartment();
        this.startDate = experience.getStartDate();
        this.endDate = experience.getEndDate();

        Resume resume = experience.getResume();
        if(resume!=null){this.resumeId = resume.getId();}

        this.createdAt = experience.getCreatedAt();
        this.updatedAt = experience.getUpdatedAt();
    }
    public static ExperienceResponseDto fromEntity(Experience experience){ return new ExperienceResponseDto(experience);}
}
