package com.team3.devinit_back.resume.repository;

import com.team3.devinit_back.resume.entity.SkillTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SkillTagRepository extends JpaRepository<SkillTag, Long> {
   Optional<SkillTag> findByName(String skillName);
}
