package com.team3.devinit_back.resume.service;

import com.team3.devinit_back.global.exception.CustomException;
import com.team3.devinit_back.global.exception.ErrorCode;
import com.team3.devinit_back.member.entity.Member;
import com.team3.devinit_back.resume.dto.ActivityRequestDto;
import com.team3.devinit_back.resume.dto.ActivityResponseDto;
import com.team3.devinit_back.resume.entity.Activity;
import com.team3.devinit_back.resume.entity.Education;
import com.team3.devinit_back.resume.entity.Resume;
import com.team3.devinit_back.resume.repository.ActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ActivityService {
    private final ActivityRepository activityRepository;

    @Transactional
    public ActivityResponseDto createActivity(Resume resume, ActivityRequestDto activityRequestDto) {
        Activity activity = Activity.builder()
                .resume(resume)
                .organization(activityRequestDto.getOrganization())
                .activityName(activityRequestDto.getActivityName())
                .description(activityRequestDto.getDescription())
                .startDate(activityRequestDto.getStartDate())
                .endDate(activityRequestDto.getEndDate())
                .build();
        Activity savedActivity = activityRepository.save(activity);

        return  ActivityResponseDto.fromEntity(savedActivity);
    }

    @Transactional
    public void updateActivity(Resume resume, Long id, ActivityRequestDto activityRequestDto) {

        Activity activity = isAuthorized(id , resume.getMember());
        activity.setActivityName(activityRequestDto.getActivityName());
        activity.setOrganization(activityRequestDto.getOrganization());
        activity.setDescription(activityRequestDto.getDescription());
        activity.setStartDate(activityRequestDto.getStartDate());
        activity.setEndDate(activityRequestDto.getEndDate());

        activityRepository.save(activity);
    }

    @Transactional
    public void deleteActivity(Resume resume, Long id) {
        Activity activity = isAuthorized(id, resume.getMember());
        activityRepository.delete(activity);
    }

    private Activity getActivityById(Long id) {
        return activityRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.ACTIVITY_NOT_FOUND));
    }

    private Activity isAuthorized(Long id, Member member) {
        Activity activity =  getActivityById(id);
        if (!activity.getResume().getMember().equals(member)) {
            throw new CustomException(ErrorCode.ACCESS_DENIED);
        }
        return activity;
    }
}
