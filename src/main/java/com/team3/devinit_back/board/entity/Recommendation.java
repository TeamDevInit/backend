package com.team3.devinit_back.board.entity;

import com.team3.devinit_back.common.BaseEntity;
import com.team3.devinit_back.member.entity.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Recommendation extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    @Builder
    public Recommendation(Member member, Board board){
        this.member = member;
        this.board = board;
    }
}
