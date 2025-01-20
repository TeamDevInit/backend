package com.team3.devinit_back.resume.entity;

import com.team3.devinit_back.member.entity.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Resume")
@Getter
@Setter
@NoArgsConstructor
public class Resume {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "name", length = 50)
    private String name;

    @Column(name = "position", length = 50)
    private String position;

    @Column(name = "summary", columnDefinition = "TEXT")
    private String summary;

    @Column(name = "portfolio", length = 2000)
    private String portfolio;

    @OneToOne
    @JoinColumn(name = "memberId", nullable = false)
    private Member member;

    @OneToMany(mappedBy = "resume", cascade = CascadeType.ALL)
    private List<Skill> skills = new ArrayList<>();

    @Builder
    public Resume(String id, String name, String position, String summary, String portfolio, Member member) {
        this.id = id;
        this.name = name;
        this.position = position;
        this.summary = summary;
        this.portfolio = portfolio;
        this.member = member;
    }
}