package com.team3.devinit_back.websocket.entity;

import com.team3.devinit_back.global.common.BaseEntity;
import com.team3.devinit_back.member.entity.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ChatPart")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChatPart extends BaseEntity {
    @Id
    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Id
    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    private Chatroom chatroom;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Authority authority;

    public enum Authority {
        OWNER,
        PARTICIPANT
    }
}