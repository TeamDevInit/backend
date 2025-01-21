package com.team3.devinit_back.resume.repository;

import com.team3.devinit_back.resume.entity.Resume;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResumeRepository extends JpaRepository<Resume, String> {
    Resume findByMemberId(String memberId);
}
