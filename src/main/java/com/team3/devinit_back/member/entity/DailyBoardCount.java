package com.team3.devinit_back.member.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
public class DailyBoardCount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "board_count")
    private int boardCount;

    @ManyToOne
    @JoinColumn(name ="member_id", nullable = false)
    private Member member;

    public DailyBoardCount(String memberId, LocalDate date, int boardCount) {
        this.member = new Member();
        this.member.setId(memberId);
        this.date = date;
        this.boardCount = boardCount;
    }

}
