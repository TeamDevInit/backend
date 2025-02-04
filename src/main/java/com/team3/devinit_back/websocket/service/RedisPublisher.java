package com.team3.devinit_back.websocket.service;

import com.team3.devinit_back.websocket.dto.ChatMessageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class RedisPublisher {
    private final RedisTemplate<String, Object> redisTemplate;
    private final ChannelTopic topic;

    public void publish(ChatMessageDto message) {
        log.info("[RedisPublisher] Redis에 메시지 전송: {}", message);
        redisTemplate.convertAndSend(topic.getTopic(), message);
    }
}