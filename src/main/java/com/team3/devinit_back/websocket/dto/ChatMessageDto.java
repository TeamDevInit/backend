package com.team3.devinit_back.websocket.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ChatMessageDto {

    public enum MessageType {
        ENTER, TALK, EXIT // 메시지 타입: 입장, 채팅, 퇴장
    }
    private MessageType type; // 메시지 타입
    private String roomId;
    private String sender; // 메시지 보낸 사람
    private String nickname;
    private String message; // 메시지
}