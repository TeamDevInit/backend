package com.team3.devinit_back.websocket.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.team3.devinit_back.websocket.dto.ChatMessageDto;
import com.team3.devinit_back.websocket.entity.ChatMessage;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@AllArgsConstructor
@Repository
public class RedisChatMessageRepository {
    private static final String CHAT_MESSAGES = "CHAT_MESSAGES:";
    private final ObjectMapper objectMapper;
    @Qualifier("chatMessageRedisTemplate")
    private final RedisTemplate<String, ChatMessage> chatMessageRedisTemplate;

    public void saveMessage(ChatMessageDto message) {
        String roomKey = CHAT_MESSAGES + message.getRoomId();

        if (message.getId() == null) {
            message.setId(chatMessageRedisTemplate.opsForValue().increment("CHAT_MESSAGE_ID"));
        }

        try {
            String jsonMessage = objectMapper.writeValueAsString(message);
            chatMessageRedisTemplate.opsForHash().put(roomKey, message.getId(), jsonMessage);
            chatMessageRedisTemplate.expire(roomKey, Duration.ofHours(25));
            log.info("[RedisChatMessageRepository] 저장 완료 - Room ID: {}, Message Id: {}, 메시지: {}",
                    message.getRoomId(), message.getId(), jsonMessage);
        } catch (JsonProcessingException e) {
            log.error("[RedisChatMessageRepository] 메시지 직렬화 실패", e);
        }
    }

    public List<ChatMessageDto> getMessages(String roomId) {
        String roomKey = CHAT_MESSAGES + roomId;
        Map<Object, Object> messagesMap = chatMessageRedisTemplate.opsForHash().entries(roomKey);
        List<ChatMessageDto> messages = new ArrayList<>();

        for (Object key : messagesMap.keySet()) {
            try {
                ChatMessageDto message = objectMapper.readValue(messagesMap.get(key).toString(), ChatMessageDto.class);
                messages.add(message);
            } catch (JsonProcessingException e) {
                log.error("[RedisChatMessageRepository] 메시지 역직렬화 실패 - Room ID: {}", roomId, e);
            }
        }

        log.info("[RedisChatMessageRepository] 조회된 메시지 개수: {}", messages.size());
        return messages;
    }

    public void clearUserMessages(String roomId, String sender) {
        String roomKey = CHAT_MESSAGES + roomId;
        chatMessageRedisTemplate.opsForHash().delete(roomKey, sender);
        log.info("[RedisChatMessageRepository] 특정 사용자 메시지 삭제 완료 - Room ID: {}, Sender: {}", roomId, sender);
    }

    public void clearMessages(String roomId) {
        String roomKey = CHAT_MESSAGES + roomId;
        chatMessageRedisTemplate.delete(roomKey);
        log.info("[RedisChatMessageRepository] 삭제 완료 - Room ID: {}", roomId);
    }

    public void saveAllToRedis(List<ChatMessageDto> messages) {
        for (ChatMessageDto message : messages) {
            saveMessage(message);
        }
    }
}