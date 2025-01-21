package com.team3.devinit_back.resume.service;

import com.team3.devinit_back.resume.entity.Resume;
import com.team3.devinit_back.resume.repository.ResumeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ResumeService {
    private  final ResumeRepository resumeRepository;

    public Resume findByMemberId(String memberId){ return resumeRepository.findByMemberId(memberId);}
}
