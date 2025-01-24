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

@Service
@RequiredArgsConstructor
public class EducationService {
    private final EducationRepository educationRepository;

    @Transactional
    public EducationResponseDto createEducation(Resume resume, EducationRequestDto educationRequestDto) {

        validateDuplicateEducation(resume.getId(), educationRequestDto);
        Education education = Education.builder()
                .resume(resume)
                .organization(educationRequestDto.getOrganization())
                .degree(educationRequestDto.getDegree())
                .major(educationRequestDto.getMajor())
                .startDate(educationRequestDto.getStartDate())
                .endDate(educationRequestDto.getEndDate())
                .status(educationRequestDto.getStatus())
                .build();

        Education savedEducation = educationRepository.save(education);

        return EducationResponseDto.fromEntity(savedEducation);
    }

    public List<EducationResponseDto> getAllEducation(Resume resume){
        return educationRepository.findAllByResumeId(resume.getId()).stream()
                .map(EducationResponseDto::fromEntity)
                .toList();
    }

    @Transactional
    public void updateEducation(Resume resume, Long id, EducationRequestDto educationRequestDto) {

        validateDuplicateEducation(resume.getId(), educationRequestDto,id);
        Education education = isAuthorized(id, resume.getMember());
        education.setOrganization(educationRequestDto.getOrganization());
        education.setDegree(educationRequestDto.getDegree());
        education.setMajor(educationRequestDto.getMajor());
        education.setStartDate(educationRequestDto.getStartDate());
        education.setEndDate(educationRequestDto.getEndDate());
        education.setStatus(educationRequestDto.getStatus());

        educationRepository.save(education);
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
