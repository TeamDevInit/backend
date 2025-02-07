package com.team3.devinit_back.resume.dto;

import com.team3.devinit_back.resume.entity.Education;
import com.team3.devinit_back.resume.entity.Resume;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@Getter
@RequiredArgsConstructor
public class EducationResponseDto {

    private Long id;
    private String organization;
    private String degree;
    private String major;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String status;
    private String resumeId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public EducationResponseDto(Education education){
        this.id = education.getId();
        this.organization = education.getOrganization();
        this.degree = education.getDegree();
        this.major = education.getMajor();
        this.startDate = education.getStartDate();
        this.endDate = education.getEndDate();
        this.status = education.getStatus();

        Resume resume = education.getResume();
        if(resume!=null){this.resumeId = resume.getId();}

        this.createdAt = education.getCreatedAt();
        this.updatedAt = education.getUpdatedAt();
    }

    public  static EducationResponseDto fromEntity(Education education) { return new EducationResponseDto(education);}
}
