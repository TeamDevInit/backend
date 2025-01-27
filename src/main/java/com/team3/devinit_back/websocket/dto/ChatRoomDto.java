package com.team3.devinit_back.websocket.dto;

import lombok.Getter;

import java.util.UUID;

@Getter
public class ChatRoomDto {
    private String roomId;
    private String name; // 채팅방 이름

    public static ChatRoomDto create(String name) {
        ChatRoomDto chatRoom = new ChatRoomDto();
        chatRoom.roomId = UUID.randomUUID().toString();
        chatRoom.name = name;
        return chatRoom;
    }
}