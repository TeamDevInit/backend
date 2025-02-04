package com.team3.devinit_back.websocket.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.team3.devinit_back.websocket.dto.ChatMessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Repository
public class RedisChatMessageRepository {
    private static final String CHAT_MESSAGES = "CHAT_MESSAGES:";
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    public void saveMessage(ChatMessageDto message) {
        String key = CHAT_MESSAGES + message.getRoomId();
        redisTemplate.opsForList().rightPush(key, message);
    }

    public List<ChatMessageDto> getMessages(String roomId) {
        String key = CHAT_MESSAGES + roomId;
        List<Object> messages = redisTemplate.opsForList().range(key, 0, -1);
        return messages.stream()
                .map(obj -> objectMapper.convertValue(obj, ChatMessageDto.class))
                .collect(Collectors.toList());
    }

    public void clearMessages(String roomId) {
        redisTemplate.delete(CHAT_MESSAGES + roomId);
    }
}