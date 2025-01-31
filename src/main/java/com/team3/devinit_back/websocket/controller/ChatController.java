package com.team3.devinit_back.websocket.controller;

import com.team3.devinit_back.websocket.dto.ChatMessageDto;
import com.team3.devinit_back.websocket.service.RedisPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ChatController {
    private final RedisPublisher redisPublisher;

    @MessageMapping("/chat/message")
    public void message(ChatMessageDto message) {
        log.info("[ChatController] 메시지 수신: {}", message);

        if (message == null || message.getSender() == null || message.getMessage() == null || message.getRoomId() == null) {
            log.warn("Invalid chat message received: {}", message);
            return;
        }

        if (ChatMessageDto.MessageType.ENTER.equals(message.getType())) {
            message.setMessage(message.getSender() + "님이 입장하셨습니다.");
        } else if (ChatMessageDto.MessageType.EXIT.equals(message.getType())) {
            message.setMessage(message.getSender() + "님이 퇴장하셨습니다.");
        }

        log.info("[ChatController] Redis로 메시지 발행: {} ", message);
        redisPublisher.publish(message);
    }
}