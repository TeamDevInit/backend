package com.team3.devinit_back.resume.entity;

import com.team3.devinit_back.member.entity.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "Resume")
public class Resume {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @OneToOne
    @JoinColumn(name = "memberId", nullable = false)
    private Member member;

    @Column(name = "name", length = 50)
    private String name;

    @Column(name = "position", length = 50)
    private String position;

    @Column(name = "summary", columnDefinition = "TEXT")
    private String summary;

    @Column(name = "portfolio", length = 2000)
    private String portfolio;
}