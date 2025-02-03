package com.team3.devinit_back.websocket.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.team3.devinit_back.websocket.dto.ChatMessageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class RedisSubscriber implements MessageListener {
    private final SimpMessageSendingOperations messagingTemplate;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ChatMessageDto chatMessage = objectMapper.readValue(message.getBody(), ChatMessageDto.class);
            log.info("[RedisSubscriber] 메시지 수신: {}", chatMessage);

            messagingTemplate.convertAndSend("/sub/chat/room/" + chatMessage.getRoomId(), chatMessage);
            log.info("[RedisSubscriber] 메시지 전송 완료: /sub/chat/room/{}", chatMessage.getRoomId());
        } catch (Exception e) {
            log.error("[RedisSubscriber] 메시지 파싱 오류", e);
        }
    }
}