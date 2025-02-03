package com.team3.devinit_back.websocket.entity;

import com.team3.devinit_back.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ChatRooms")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class ChatRoom extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(length = 36, nullable = false)
    private String id;

    @Column(length = 50, nullable = true)
    private String name;

//    @Column(length = 30, nullable = true)
//    private String password;

//    @Column(length = 2000, nullable = true)
//    private String chatUrl;

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatPart> chatParts = new ArrayList<>();
}