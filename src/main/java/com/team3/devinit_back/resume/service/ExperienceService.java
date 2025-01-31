package com.team3.devinit_back.resume.service;

import com.team3.devinit_back.global.exception.CustomException;
import com.team3.devinit_back.global.exception.ErrorCode;
import com.team3.devinit_back.member.entity.Member;
import com.team3.devinit_back.resume.dto.*;
import com.team3.devinit_back.resume.entity.Activity;
import com.team3.devinit_back.resume.entity.Experience;
import com.team3.devinit_back.resume.entity.Project;
import com.team3.devinit_back.resume.entity.Resume;
import com.team3.devinit_back.resume.repository.ExperienceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExperienceService {
    private final ExperienceRepository experienceRepository;

    @Transactional
    public List<ExperienceResponseDto> createExperiences(Resume resume, List<ExperienceRequestDto> experienceRequestDtos) {
        List<Experience> experiences = experienceRequestDtos.stream()
                .map(experienceRequestDto -> Experience.builder()
                    .resume(resume)
                    .companyName(experienceRequestDto.getCompanyName())
                    .department(experienceRequestDto.getDepartment())
                    .description(experienceRequestDto.getDescription())
                    .employmentType(experienceRequestDto.getEmploymentType())
                    .position(experienceRequestDto.getPosition())
                    .startDate(experienceRequestDto.getStartDate())
                    .endDate(experienceRequestDto.getEndDate())
                    .build())
                .toList();

        List<Experience> savedExperiences = experienceRepository.saveAll(experiences);

        return savedExperiences.stream()
                .map(ExperienceResponseDto::fromEntity)
                .toList();
    }

    public List<ExperienceResponseDto> getAllExperiences(Resume resume){
        return experienceRepository.findAllByResumeId(resume.getId()).stream()
                .map(ExperienceResponseDto::fromEntity)
                .toList();
    }

    @Transactional
    public void updateExperiences(Resume resume, List<ExperienceRequestDto> experienceRequestDtos){
       List<Experience> updatedExperiences = experienceRequestDtos.stream()
               .map(experienceRequestDto -> {
                   Long id = experienceRequestDto.getId();
                   Experience experience = isAuthorized(id, resume.getMember());
                   experience.setCompanyName(experienceRequestDto.getCompanyName());
                   experience.setDepartment(experienceRequestDto.getDepartment());
                   experience.setDescription(experienceRequestDto.getDescription());
                   experience.setEmploymentType(experienceRequestDto.getEmploymentType());
                   experience.setPosition(experienceRequestDto.getPosition());
                   experience.setStartDate(experienceRequestDto.getStartDate());
                   experience.setEndDate(experienceRequestDto.getEndDate());
                   return experience;

               })
               .toList();

        experienceRepository.saveAll(updatedExperiences);
    }
    @Transactional
    public List<ExperienceResponseDto> saveOrUpdateExperiences(Resume resume, List<ExperienceRequestDto> experienceRequestDtos) {
        List<Experience> experiences = experienceRequestDtos.stream()
                .map(dto -> {
                    if (dto.getId() == null) {
                        return Experience.builder()
                                .resume(resume)
                                .companyName(dto.getCompanyName())
                                .department(dto.getDepartment())
                                .description(dto.getDescription())
                                .employmentType(dto.getEmploymentType())
                                .position(dto.getPosition())
                                .startDate(dto.getStartDate())
                                .endDate(dto.getEndDate())
                                .build();
                    } else {
                        return experienceRepository.findById(dto.getId())
                                .map(experience -> {
                                    experience.setCompanyName(dto.getCompanyName());
                                    experience.setDepartment(dto.getDepartment());
                                    experience.setDescription(dto.getDescription());
                                    experience.setEmploymentType(dto.getEmploymentType());
                                    experience.setPosition(dto.getPosition());
                                    experience.setStartDate(dto.getStartDate());
                                    experience.setEndDate(dto.getEndDate());
                                    return experience;
                                }).orElseThrow(() -> new CustomException(ErrorCode.EXPERIENCE_NOT_FOUND));
                    }
                })
                .toList();

        // DB에 저장
        List<Experience> savedExperiences = experienceRepository.saveAll(experiences);

        // DTO 변환 후 반환
        return savedExperiences.stream()
                .map(ExperienceResponseDto::fromEntity)
                .toList();
    }
    @Transactional
    public void deleteExperience(Resume resume, Long id){
        Experience experience = isAuthorized(id, resume.getMember());
        experienceRepository.delete(experience);
    }

    private Experience getExperienceById(Long id){
        return experienceRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.EXPERIENCE_NOT_FOUND));
    }

    private Experience isAuthorized(Long id, Member member) {
        Experience experience =  getExperienceById(id);
        if (!experience.getResume().getMember().equals(member)) {
            throw new CustomException(ErrorCode.ACCESS_DENIED);
        }
        return experience;
    }
}


