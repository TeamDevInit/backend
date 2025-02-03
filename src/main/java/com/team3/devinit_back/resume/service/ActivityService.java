package com.team3.devinit_back.resume.service;

import com.team3.devinit_back.global.exception.CustomException;
import com.team3.devinit_back.global.exception.ErrorCode;
import com.team3.devinit_back.member.entity.Member;
import com.team3.devinit_back.resume.dto.ActivityRequestDto;
import com.team3.devinit_back.resume.dto.ActivityResponseDto;
import com.team3.devinit_back.resume.entity.Activity;
import com.team3.devinit_back.resume.entity.Resume;
import com.team3.devinit_back.resume.repository.ActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ActivityService {
    private final ActivityRepository activityRepository;

    @Transactional
    public List<ActivityResponseDto> createActivities(Resume resume, List<ActivityRequestDto> activityRequestDtos) {
        List<Activity> activities = activityRequestDtos.stream()
                .map(dto -> Activity.builder()
                        .resume(resume)
                        .organization(dto.getOrganization())
                        .activityName(dto.getActivityName())
                        .description(dto.getDescription())
                        .startDate(dto.getStartDate())
                        .endDate(dto.getEndDate())
                        .build())
                .toList();

        List<Activity> savedActivities = activityRepository.saveAll(activities);

        return savedActivities.stream()
                .map(ActivityResponseDto::fromEntity)
                .toList();
    }

    public List<ActivityResponseDto> getAllActivity(Resume resume){
        return activityRepository.findAllByResumeId(resume.getId()).stream()
                .map(ActivityResponseDto::fromEntity)
                .toList();
    }

    @Transactional
    public void updateActivities(Resume resume, List<ActivityRequestDto> activityRequestDtos) {
        List<Activity> updatedActivities = activityRequestDtos.stream()
                .map(activityRequestDto -> {
                    Long id = activityRequestDto.getId();
                    Activity activity = isAuthorized(id, resume.getMember());
                    activity.setActivityName(activityRequestDto.getActivityName());
                    activity.setOrganization(activityRequestDto.getOrganization());
                    activity.setDescription(activityRequestDto.getDescription());
                    activity.setStartDate(activityRequestDto.getStartDate());
                    activity.setEndDate(activityRequestDto.getEndDate());
                    return activity;
                })
                .toList();

        activityRepository.saveAll(updatedActivities);
    }

    @Transactional
    public List<ActivityResponseDto> saveOrUpdateActivities(Resume resume, List<ActivityRequestDto> activityRequestDtos) {
        List<Activity> activities = activityRequestDtos.stream()
                .map(dto -> {
                    if (dto.getId() == null) {
                        return Activity.builder()
                                .resume(resume)
                                .organization(dto.getOrganization())
                                .activityName(dto.getActivityName())
                                .description(dto.getDescription())
                                .startDate(dto.getStartDate())
                                .endDate(dto.getEndDate())
                                .build();
                    } else {
                        return activityRepository.findById(dto.getId())
                                .map(activity -> {
                                    activity.setActivityName(dto.getActivityName());
                                    activity.setOrganization(dto.getOrganization());
                                    activity.setDescription(dto.getDescription());
                                    activity.setStartDate(dto.getStartDate());
                                    activity.setEndDate(dto.getEndDate());
                                    return activity;
                                }).orElseThrow(() -> new CustomException(ErrorCode.ACTIVITY_NOT_FOUND));
                    }
                })
                .toList();

        // DB에 저장
        List<Activity> savedActivities = activityRepository.saveAll(activities);

        // DTO 변환 후 반환
        return savedActivities.stream()
                .map(ActivityResponseDto::fromEntity)
                .toList();
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
