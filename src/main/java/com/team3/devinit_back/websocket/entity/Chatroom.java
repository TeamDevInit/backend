package com.team3.devinit_back.websocket.entity;

import com.team3.devinit_back.global.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Chatrooms")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Chatroom extends BaseEntity {
    @Id
    @Column(length = 36, nullable = false)
    private String id;

    @Column(length = 50, nullable = true)
    private String name;

    @Column(length = 30, nullable = true)
    private String password;

    @Column(length = 2000, nullable = true)
    private String chatUrl;
}