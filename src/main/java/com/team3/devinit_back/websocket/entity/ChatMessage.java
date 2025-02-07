package com.team3.devinit_back.websocket.entity;

import com.team3.devinit_back.global.common.BaseEntity;
import com.team3.devinit_back.member.entity.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ChatMessages")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessage extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private ChatRoom chatRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    private Member sender;

    @Enumerated(EnumType.STRING)
    @Column(name = "msgType", nullable = false)
    private MessageType msgType;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(name = "contentType", nullable = true)
    private ContentType contentType;

    public enum MessageType {
        ENTER, TALK, EXIT
    }

    public enum ContentType {
        TEXT, IMAGE, URL
    }
}