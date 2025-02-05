package com.team3.devinit_back.resume.service;

import com.team3.devinit_back.global.common.AbstractResumeService;
import com.team3.devinit_back.global.exception.CustomException;
import com.team3.devinit_back.global.exception.ErrorCode;
import com.team3.devinit_back.resume.dto.ActivityRequestDto;
import com.team3.devinit_back.resume.dto.ActivityResponseDto;
import com.team3.devinit_back.resume.entity.Activity;
import com.team3.devinit_back.resume.entity.Resume;
import com.team3.devinit_back.resume.repository.ActivityRepository;
import org.springframework.stereotype.Service;

@Service
public class ActivityService extends AbstractResumeService<Activity, ActivityRequestDto, ActivityResponseDto, ActivityRepository> {

    public ActivityService(ActivityRepository repository) {
        super(repository);
    }

    @Override
    protected Activity toEntity(ActivityRequestDto dto, Resume resume) {
        return Activity.builder()
                .resume(resume)
                .organization(dto.getOrganization())
                .activityName(dto.getActivityName())
                .description(dto.getDescription())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .build();
    }

    @Override
    protected void updateEntity(Activity entity, ActivityRequestDto dto) {
        entity.setActivityName(dto.getActivityName());
        entity.setOrganization(dto.getOrganization());
        entity.setDescription(dto.getDescription());
        entity.setStartDate(dto.getStartDate());
        entity.setEndDate(dto.getEndDate());
    }

    @Override
    protected ActivityResponseDto toResponseDto(Activity entity) {
        return ActivityResponseDto.fromEntity(entity);
    }

    @Override
    protected Activity getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.ACTIVITY_NOT_FOUND));
    }

    @Override
    protected boolean isDuplicate(Resume resume, ActivityRequestDto dto, Long excludeId) {
        return false;
    }

    @Override
    protected Long getIdFromRequestDto(ActivityRequestDto dto) {
        return dto.getId();
    }
}
