package com.team3.devinit_back.resume.dto;

import com.team3.devinit_back.resume.entity.Activity;
import com.team3.devinit_back.resume.entity.Resume;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;

@Data
@Getter
public class ActivityResponseDto {

    private Long id;
    private String activityName;
    private String organization;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String description;
    private String resumeId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ActivityResponseDto(Activity activity){
        this.activityName = activity.getActivityName();
        this.organization = activity.getOrganization();
        this.description = activity.getDescription();
        this.startDate = activity.getStartDate();
        this.endDate = activity.getEndDate();

        Resume resume = activity.getResume();
        if(resume!=null){this.resumeId = resume.getId();}

        this.createdAt = activity.getCreatedAt();
        this.updatedAt = activity.getUpdatedAt();
    }
    public static ActivityResponseDto fromEntity(Activity activity){ return  new ActivityResponseDto(activity);}
}
