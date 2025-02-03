package com.team3.devinit_back.websocket.dto;

import com.team3.devinit_back.member.entity.Member;
import com.team3.devinit_back.websocket.entity.ChatRoom;
import lombok.Data;
import lombok.Getter;

import java.io.Serializable;

@Getter
@Data
public class ChatRoomDetailDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private String roomId;
    private String name;
    private String ownerNickname;

    public static ChatRoomDetailDto fromEntity(ChatRoom chatRoom, Member owner) {
        ChatRoomDetailDto dto = new ChatRoomDetailDto();
        dto.roomId = chatRoom.getId();
        dto.name = chatRoom.getName();
        dto.ownerNickname = owner.getNickName();
        return dto;
    }
}