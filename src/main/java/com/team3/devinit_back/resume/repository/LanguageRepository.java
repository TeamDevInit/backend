package com.team3.devinit_back.resume.repository;

import com.team3.devinit_back.resume.entity.Language;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LanguageRepository extends JpaRepository<Language,Long> {
    List<Language> findAllByResumeId(String id);

    Optional<Language> findByResumeIdAndName(String id, String name);
}
