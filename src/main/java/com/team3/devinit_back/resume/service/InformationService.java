package com.team3.devinit_back.resume.service;


import com.team3.devinit_back.global.exception.CustomException;
import com.team3.devinit_back.global.exception.ErrorCode;
import com.team3.devinit_back.member.entity.Member;
import com.team3.devinit_back.resume.dto.InformationRequestDto;
import com.team3.devinit_back.resume.dto.InformationResponseDto;
import com.team3.devinit_back.resume.entity.Information;
import com.team3.devinit_back.resume.entity.Resume;
import com.team3.devinit_back.resume.repository.InformationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InformationService {
    private final InformationRepository informationRepository;

    @Transactional
    public InformationResponseDto createInformation(Resume resume, InformationRequestDto informationRequestDto) {
        Information information = Information.builder()
                .resume(resume)
                .name(informationRequestDto.getName())
                .position(informationRequestDto.getPosition())
                .summary(informationRequestDto.getSummary())
                .employmentPeriod(informationRequestDto.getEmploymentPeriod())
                .portfolio(informationRequestDto.getPortfolio())
                .build();

        Information savedInformation = informationRepository.save(information);

        return InformationResponseDto.fromEntity(savedInformation);
    }

    public InformationResponseDto getInformation(Resume resume){

        Information information = informationRepository.findByResumeId(resume.getId());

        return InformationResponseDto.fromEntity(information);
    }

    @Transactional
    public void updateInformation(Resume resume, Long id, InformationRequestDto informationRequestDto){

        Information information = isAuthorized(id, resume.getMember());
        information.setName(informationRequestDto.getName());
        information.setPosition(informationRequestDto.getPosition());
        information.setSummary(informationRequestDto.getSummary());
        information.setEmploymentPeriod(informationRequestDto.getEmploymentPeriod());
        information.setPortfolio(informationRequestDto.getPortfolio());

        informationRepository.save(information);
    }

    @Transactional
    public void deleteInformation(Resume resume, Long id){
        Information information = isAuthorized(id, resume.getMember());
        informationRepository.delete(information);
    }

    private Information getInformationById(Long id) {
        return informationRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.INFORMATION_NOT_FOUND));
    }

    private Information isAuthorized(Long id, Member member) {
        Information Information =  getInformationById(id);
        if (!Information.getResume().getMember().equals(member)) {
            throw new CustomException(ErrorCode.ACCESS_DENIED);
        }
        return Information;
    }

}
