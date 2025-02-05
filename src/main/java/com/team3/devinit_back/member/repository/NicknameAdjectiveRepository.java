package com.team3.devinit_back.member.repository;


import com.team3.devinit_back.member.entity.NicknameAdjective;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface NicknameAdjectiveRepository extends JpaRepository<NicknameAdjective, Long> {

    @Query(value = "SELECT * FROM NicknameAdjective ORDER BY RAND() LIMIT 1", nativeQuery = true)
    NicknameAdjective getRandomAdjective();
}