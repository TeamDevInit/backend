package com.team3.devinit_back.resume.entity;

import com.team3.devinit_back.global.common.BaseEntity;
import com.team3.devinit_back.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "Resume")
@Getter
@Setter
@RequiredArgsConstructor
public class Resume extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @OneToMany(mappedBy = "resume", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Skill> skills = new LinkedHashSet<>();

    @OneToOne(mappedBy = "resume", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Information information;

    @OneToMany(mappedBy = "resume", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Activity> activities = new LinkedHashSet<>();

    @OneToMany(mappedBy = "resume", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Education> educations = new LinkedHashSet<>();

    @OneToMany(mappedBy = "resume", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Experience> experiences = new LinkedHashSet<>();

    @OneToMany(mappedBy = "resume", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Language> languages = new LinkedHashSet<>();

    @OneToMany(mappedBy = "resume", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Project> projects = new LinkedHashSet<>();
    public Resume(String id, Member member) {
        this.id = id;
        this.member = member;
    }
}