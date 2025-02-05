package com.team3.devinit_back.member.repository;

import com.team3.devinit_back.member.entity.NicknameNoun;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface NicknameNounRepository extends JpaRepository<NicknameNoun, Long> {

    @Query(value = "SELECT * FROM NicknameNoun ORDER BY RAND() LIMIT 1", nativeQuery = true)
    NicknameNoun getRandomNoun();
}