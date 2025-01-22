package com.team3.devinit_back.resume.service;

import com.team3.devinit_back.global.exception.CustomException;
import com.team3.devinit_back.global.exception.ErrorCode;
import com.team3.devinit_back.member.entity.Member;
import com.team3.devinit_back.resume.dto.LanguageRequestDto;
import com.team3.devinit_back.resume.dto.LanguageResponseDto;
import com.team3.devinit_back.resume.entity.Language;
import com.team3.devinit_back.resume.entity.Resume;
import com.team3.devinit_back.resume.repository.LanguageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LanguageService {
    private final LanguageRepository languageRepository;

    @Transactional
    public LanguageResponseDto createLanguage(Resume resume, LanguageRequestDto languageRequestDto) {

        validateDuplicateLanguage(resume.getId(), languageRequestDto.getName());
        Language language = Language.builder()
                .resume(resume)
                .level(languageRequestDto.getLevel())
                .name(languageRequestDto.getName())
                .build();

        Language savedLanguage = languageRepository.save(language);

        return LanguageResponseDto.fromEntity(savedLanguage);
    }

    public List<LanguageResponseDto> getAllLanguage(Resume resume){
        return languageRepository.findAllByResumeId(resume.getId()).stream()
                .map(LanguageResponseDto::fromEntity)
                .toList();
    }

    @Transactional
    public void updateLanguage(Resume resume, Long id, LanguageRequestDto languageRequestDto) {

        validateDuplicateLanguage(resume.getId(), languageRequestDto.getName(),id);
        Language language = isAuthorized(id, resume.getMember());
        language.setName(languageRequestDto.getName());
        language.setLevel(languageRequestDto.getLevel());

        languageRepository.save(language);
    }

    @Transactional
    public void deleteLanguage(Resume resume, Long id) {
        Language language = isAuthorized(id, resume.getMember());
        languageRepository.delete(language);
    }

    private void validateDuplicateLanguage(String resumeId, String name) {
        languageRepository.findByResumeIdAndName(resumeId, name)
                .ifPresent(language -> {
                    throw new CustomException(ErrorCode.DUPLICATE_LANGUAGE);
                });
    }

    private void validateDuplicateLanguage(String resumeId, String name, Long excludeId) {
        languageRepository.findByResumeIdAndName(resumeId, name)
                .filter(language -> !language.getId().equals(excludeId))
                .ifPresent(language -> {
                    throw new CustomException(ErrorCode.DUPLICATE_LANGUAGE);
                });
    }

    private Language getLanguageById(Long id) {
        return languageRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.LANGUAGE_NOT_FOUND));
    }

    private Language isAuthorized(Long id, Member member) {
        Language language =  getLanguageById(id);
        if (!language.getResume().getMember().equals(member)) {
            throw new CustomException(ErrorCode.ACCESS_DENIED);
        }
        return language;
    }


}
