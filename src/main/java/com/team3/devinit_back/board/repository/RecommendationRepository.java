package com.team3.devinit_back.board.repository;

import com.team3.devinit_back.board.entity.Board;
import com.team3.devinit_back.board.entity.Recommendation;
import com.team3.devinit_back.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RecommendationRepository extends JpaRepository<Recommendation,Long> {
    Optional<Recommendation> findByBoardAndMember(Board board, Member member);

    int countByBoard(Board board);

    boolean existsByBoardAndMember(Board board, Member member);
}
