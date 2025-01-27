package com.team3.devinit_back.resume.entity;

import com.team3.devinit_back.global.common.BaseEntity;
import com.team3.devinit_back.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Resume")
@Getter
@Setter
@RequiredArgsConstructor
public class Resume extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @OneToOne
    @JoinColumn(name = "memberId", nullable = false)
    private Member member;

    @OneToMany(mappedBy = "resume", cascade = CascadeType.ALL)
    private List<Skill> skills = new ArrayList<>();


    public Resume(String id, Member member) {
        this.id = id;
        this.member = member;
    }
}