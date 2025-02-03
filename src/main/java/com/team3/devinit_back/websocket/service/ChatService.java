package com.team3.devinit_back.websocket.service;

import com.team3.devinit_back.global.exception.CustomException;
import com.team3.devinit_back.global.exception.ErrorCode;
import com.team3.devinit_back.websocket.dto.ChatMessageDto;
import com.team3.devinit_back.websocket.repository.RedisChatMessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatService {
    private final RedisPublisher redisPublisher;
    private final RedisChatMessageRepository redisChatMessageRepository;

    public void processMessage(ChatMessageDto message) {
        log.info("[ChatService] 메시지 처리 중: {}", message);

        if (message.getSender() == null || message.getSender().isEmpty()) {
            log.error("sender가 null입니다.");
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        log.info("채팅 메시지 전송: {}", message);
        redisChatMessageRepository.saveMessage(message);
        redisPublisher.publish(message);
    }
}