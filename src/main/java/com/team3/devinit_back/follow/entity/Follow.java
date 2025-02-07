package com.team3.devinit_back.follow.entity;

import com.team3.devinit_back.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "Follows")
public class Follow extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "sender_id", nullable = false)
    private String senderId; // 팔로우를 요청한 사용자 ID

    @Column(name = "receiver_id", nullable = false)
    private String receiverId; // 팔로우를 받은 사용자 ID
}