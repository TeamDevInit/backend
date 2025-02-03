package com.team3.devinit_back.websocket.repository;

import com.team3.devinit_back.member.entity.Member;
import com.team3.devinit_back.websocket.entity.ChatPart;
import com.team3.devinit_back.websocket.entity.ChatPartId;
import com.team3.devinit_back.websocket.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ChatPartRepository extends JpaRepository<ChatPart, ChatPartId> {
    Optional<ChatPart> findByChatRoom_IdAndAuthority(String chatRoomId, ChatPart.Authority authority);

    List<ChatPart> findByMember(Member member);

    ChatPart findByMemberAndChatRoom(Member member, ChatRoom chatRoom);

    void deleteByChatRoom(ChatRoom chatRoom);
}