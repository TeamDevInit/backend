package com.team3.devinit_back.resume.service;

import com.team3.devinit_back.global.exception.CustomException;
import com.team3.devinit_back.global.exception.ErrorCode;
import com.team3.devinit_back.member.entity.Member;
import com.team3.devinit_back.resume.dto.ActivityRequestDto;
import com.team3.devinit_back.resume.dto.ActivityResponseDto;
import com.team3.devinit_back.resume.dto.LanguageRequestDto;
import com.team3.devinit_back.resume.dto.LanguageResponseDto;
import com.team3.devinit_back.resume.entity.Activity;
import com.team3.devinit_back.resume.entity.Language;
import com.team3.devinit_back.resume.entity.Resume;
import com.team3.devinit_back.resume.repository.LanguageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LanguageService {
    private final LanguageRepository languageRepository;

    @Transactional
    public List<LanguageResponseDto> createLanguages(Resume resume, List<LanguageRequestDto> languageRequestDtos) {
        languageRequestDtos.forEach(languageRequestDto -> validateDuplicateLanguage(resume.getId(), languageRequestDto.getName()));
        List<Language> languages = languageRequestDtos.stream()
                .map(languageRequestDto -> Language.builder()
                        .resume(resume)
                        .level(languageRequestDto.getLevel())
                        .name(languageRequestDto.getName())
                        .build())
                .toList();

        List<Language> savedLanguages = languageRepository.saveAll(languages);

        return savedLanguages.stream()
                .map(LanguageResponseDto::fromEntity)
                .toList();
    }

    public List<LanguageResponseDto> getAllLanguage(Resume resume){
        return languageRepository.findAllByResumeId(resume.getId()).stream()
                .map(LanguageResponseDto::fromEntity)
                .toList();
    }

    @Transactional
    public void updateLanguages(Resume resume, List<LanguageRequestDto> languageRequestDtos) {
        languageRequestDtos.forEach(languageRequestDto -> validateDuplicateLanguage(resume.getId(), languageRequestDto.getName(), languageRequestDto.getId()));
        List<Language> updatedLanguages = languageRequestDtos.stream()
                .map(languageRequestDto -> {
                    Long id = languageRequestDto.getId();
                    Language language = isAuthorized(id, resume.getMember());
                    language.setName(languageRequestDto.getName());
                    language.setLevel(languageRequestDto.getLevel());
                    return  language;
                })
                .toList();

        languageRepository.saveAll(updatedLanguages);
    }

    @Transactional
    public List<LanguageResponseDto> saveOrUpdateLanguages(Resume resume,List<LanguageRequestDto> languageRequestDtos) {
        List<Language> languages = languageRequestDtos.stream()
                .map(dto -> {
                    if (dto.getId() == null) {
                        return Language.builder()
                                .resume(resume)
                                .level(dto.getLevel())
                                .name(dto.getName())
                                .build();
                    } else {
                        return languageRepository.findById(dto.getId())
                                .map(language -> {
                                    language.setName(dto.getName());
                                    language.setLevel(dto.getLevel());;
                                    return language;
                                }).orElseThrow(() -> new CustomException(ErrorCode.LANGUAGE_NOT_FOUND));
                    }
                })
                .toList();

        List<Language> savedLanguages = languageRepository.saveAll(languages);

        return savedLanguages.stream()
                .map(LanguageResponseDto::fromEntity)
                .toList();
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
