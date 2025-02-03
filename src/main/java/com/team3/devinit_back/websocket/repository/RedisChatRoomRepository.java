package com.team3.devinit_back.websocket.repository;

import com.team3.devinit_back.websocket.dto.ChatRoomListDto;
import com.team3.devinit_back.websocket.entity.ChatRoom;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class RedisChatRoomRepository {
    private static final String CHAT_ROOMS = "CHAT_ROOM";
    private final RedisTemplate<String, Object> redisTemplate;

    public ChatRoomListDto findRoomById(String roomId) {
        return (ChatRoomListDto) redisTemplate.opsForHash().get(CHAT_ROOMS, roomId);
    }

    public void saveChatRoom(ChatRoom chatRoom) {
        ChatRoomListDto chatRoomListDto = ChatRoomListDto.fromEntity(chatRoom);
        redisTemplate.opsForHash().put(CHAT_ROOMS, chatRoomListDto.getRoomId(), chatRoomListDto);
    }

    public void deleteChatRoom(String roomId) {
        redisTemplate.opsForHash().delete(CHAT_ROOMS, roomId);
    }
}