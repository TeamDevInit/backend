package com.team3.devinit_back.member.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "Members")
public class MemberEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    //private String name;

    @Column(nullable = false, unique = true)
    private String nickName;

    @Column(nullable = false, unique = true)
    private String socialId;
    private String socialProvider;
    private String role;

}
