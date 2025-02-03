package com.team3.devinit_back.websocket.dto;

import com.team3.devinit_back.member.entity.Member;
import com.team3.devinit_back.websocket.entity.ChatRoom;
import lombok.Data;
import lombok.Getter;

import java.io.Serializable;

@Getter
@Data
public class ChatRoomListDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private String roomId;
    private String name; // 채팅방 이름

    public static ChatRoomListDto fromEntity(ChatRoom chatRoom) {
        ChatRoomListDto dto = new ChatRoomListDto();
        dto.roomId = chatRoom.getId();
        dto.name = chatRoom.getName();
        return dto;
    }
}