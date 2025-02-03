package com.team3.devinit_back.resume.service;

import com.team3.devinit_back.global.exception.CustomException;
import com.team3.devinit_back.global.exception.ErrorCode;
import com.team3.devinit_back.member.entity.Member;
import com.team3.devinit_back.resume.dto.ActivityRequestDto;
import com.team3.devinit_back.resume.dto.ActivityResponseDto;
import com.team3.devinit_back.resume.dto.EducationRequestDto;
import com.team3.devinit_back.resume.dto.EducationResponseDto;
import com.team3.devinit_back.resume.entity.Education;
import com.team3.devinit_back.resume.entity.Resume;
import com.team3.devinit_back.resume.repository.EducationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EducationService {
    private final EducationRepository educationRepository;

    @Transactional
    public List<EducationResponseDto> createEducations(Resume resume, List<EducationRequestDto> educationRequestDtos) {
        educationRequestDtos.forEach(educationRequestDto -> validateDuplicateEducation(resume.getId(), educationRequestDto));

        List<Education> educations = educationRequestDtos.stream()
                .map(educationRequestDto -> Education.builder()
                        .resume(resume)
                        .organization(educationRequestDto.getOrganization())
                        .degree(educationRequestDto.getDegree())
                        .major(educationRequestDto.getMajor())
                        .startDate(educationRequestDto.getStartDate())
                        .endDate(educationRequestDto.getEndDate())
                        .status(educationRequestDto.getStatus())
                        .build())
                .toList();

        List<Education> savedEducations = educationRepository.saveAll(educations);

        return savedEducations.stream()
                .map(EducationResponseDto::fromEntity)
                .toList();
    }

    public List<EducationResponseDto> getAllEducation(Resume resume){
        return educationRepository.findAllByResumeId(resume.getId()).stream()
                .map(EducationResponseDto::fromEntity)
                .toList();
    }

    @Transactional
    public void updateEducations(Resume resume, List<EducationRequestDto> educationRequestDtos) {

        educationRequestDtos.forEach(dto -> validateDuplicateEducation(resume.getId(), dto));

        List<Education> updatedEducations = educationRequestDtos.stream()
                .map(educationRequestDto -> {
                    Long id = educationRequestDto.getId();
                    Education education = isAuthorized(id, resume.getMember());
                    education.setOrganization(educationRequestDto.getOrganization());
                    education.setDegree(educationRequestDto.getDegree());
                    education.setMajor(educationRequestDto.getMajor());
                    education.setStartDate(educationRequestDto.getStartDate());
                    education.setEndDate(educationRequestDto.getEndDate());
                    education.setStatus(educationRequestDto.getStatus());
                    return education;
                })
                .toList();

        educationRepository.saveAll(updatedEducations);
    }
    @Transactional
    public List<EducationResponseDto> saveOrUpdateEducations(Resume resume,List<EducationRequestDto> educationRequestDtos){
        List<Education> educations = educationRequestDtos.stream()
                .map(dto -> {
                    if (dto.getId() == null) {
                        return Education.builder()
                                .resume(resume)
                                .organization(dto.getOrganization())
                                .degree(dto.getDegree())
                                .major(dto.getMajor())
                                .status(dto.getStatus())
                                .startDate(dto.getStartDate())
                                .endDate(dto.getEndDate())
                                .build();
                    } else {
                        return educationRepository.findById(dto.getId())
                                .map(education -> {
                                    education.setOrganization(dto.getOrganization());
                                    education.setDegree(dto.getDegree());
                                    education.setMajor(dto.getMajor());
                                    education.setStartDate(dto.getStartDate());
                                    education.setEndDate(dto.getEndDate());
                                    education.setStatus(dto.getStatus());
                                    return education;
                                }).orElseThrow(() -> new CustomException(ErrorCode.EDUCATION_NOT_FOUND));
                    }
                })
                .toList();
        List<Education> savedEducations = educationRepository.saveAll(educations);

        return savedEducations.stream()
                .map(EducationResponseDto::fromEntity)
                .toList();
    }

    @Transactional
    public void deleteEducation(Resume resume, Long id) {
        Education education = isAuthorized(id, resume.getMember());
        educationRepository.delete(education);
    }

    private void validateDuplicateEducation(String resumeId, EducationRequestDto educationRequestDto){
        educationRepository.findByResumeIdAndOrganizationAndDegreeAndMajor(
                resumeId,educationRequestDto.getOrganization(),educationRequestDto.getDegree(), educationRequestDto.getMajor())
                .ifPresent(education -> {
                    throw new CustomException(ErrorCode.DUPLICATE_EDUCATION);
                });
    }

    private void validateDuplicateEducation(String resumeId, EducationRequestDto educationRequestDto, Long excludeId){
        educationRepository.findByResumeIdAndOrganizationAndDegreeAndMajor(
                        resumeId,educationRequestDto.getOrganization(),educationRequestDto.getDegree(), educationRequestDto.getMajor())
                .filter(education -> !education.getId().equals(excludeId))
                .ifPresent(education -> {
                    throw new CustomException(ErrorCode.DUPLICATE_EDUCATION);
                });
    }

    private Education getEducationById(Long id) {
        return educationRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.EDUCATION_NOT_FOUND));
    }

    private Education isAuthorized(Long id, Member member) {
        Education education =  getEducationById(id);
        if (!education.getResume().getMember().equals(member)) {
            throw new CustomException(ErrorCode.ACCESS_DENIED);
        }
        return education;
    }

}
