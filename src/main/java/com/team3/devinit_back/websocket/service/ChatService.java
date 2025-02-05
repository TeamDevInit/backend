package com.team3.devinit_back.websocket.service;

import com.team3.devinit_back.global.exception.CustomException;
import com.team3.devinit_back.global.exception.ErrorCode;
import com.team3.devinit_back.websocket.dto.ChatMessageDto;
import com.team3.devinit_back.websocket.repository.RedisChatMessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatService {
    private final RedisTemplate<String, Object> redisTemplate;
    private final RedisChatMessageRepository redisChatMessageRepository;
    private final ChannelTopic channelTopic;

    public void processMessage(ChatMessageDto message) {
        log.info("[ChatService] 메시지 처리 중: {}", message);

        if (message.getSender() == null || message.getSender().isEmpty()) {
            log.error("sender가 null입니다.");
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        if (message.getType() == ChatMessageDto.MessageType.ENTER) {
            message.setMessage(message.getNickname() + "님이 입장하셨습니다.");
        } else if (message.getType() == ChatMessageDto.MessageType.EXIT) {
            message.setMessage(message.getNickname() + "님이 퇴장하셨습니다.");
        }

        log.info("[ChatService] 채팅 메시지 전송: {}", message);
        redisChatMessageRepository.saveMessage(message);
        redisTemplate.convertAndSend(channelTopic.getTopic(), message);
    }
}