package com.team3.devinit_back.member.repository;

import com.team3.devinit_back.member.entity.DailyBoardCount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DailyBoardCountRepository extends JpaRepository<DailyBoardCount, Long> {
    Optional<DailyBoardCount> findByMemberIdAndDate(String id, LocalDate date);
    List<DailyBoardCount> findByMemberIdAndDateBetween(String memberId, LocalDate start, LocalDate end);
}
