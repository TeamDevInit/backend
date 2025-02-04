package com.team3.devinit_back.websocket.service;

import com.team3.devinit_back.global.exception.CustomException;
import com.team3.devinit_back.global.exception.ErrorCode;
import com.team3.devinit_back.member.entity.Member;
import com.team3.devinit_back.member.repository.MemberRepository;
import com.team3.devinit_back.websocket.dto.ChatRoomDetailDto;
import com.team3.devinit_back.websocket.dto.ChatRoomListDto;
import com.team3.devinit_back.websocket.entity.ChatPart;
import com.team3.devinit_back.websocket.entity.ChatPartId;
import com.team3.devinit_back.websocket.entity.ChatRoom;
import com.team3.devinit_back.websocket.repository.ChatPartRepository;
import com.team3.devinit_back.websocket.repository.ChatRoomRepository;
import com.team3.devinit_back.websocket.repository.RedisChatRoomRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;
    private final ChatPartRepository chatPartRepository;
    private final RedisChatRoomRepository redisChatRoomRepository;
    private final MemberRepository memberRepository;

    public List<ChatRoomListDto> getAllChatRooms() {
        List<ChatRoom> chatRooms = chatRoomRepository.findAll();
        return chatRooms.stream()
                .map(ChatRoomListDto::fromEntity)
                .collect(Collectors.toList());
    }

    public ChatRoomDetailDto getChatRoomById(String roomId, Member member) {
        if (member == null) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }
        ChatRoom chatRoom = findChatRoomById(roomId);
        Member owner = getChatRoomOwner(roomId);
        return ChatRoomDetailDto.fromEntity(chatRoom, owner);
    }

    public List<ChatRoomListDto> getMyChatRooms(Member member) {
        List<ChatPart> chatParts = chatPartRepository.findByMember(member);
        return chatParts.stream()
                .map(chatPart -> ChatRoomListDto.fromEntity(chatPart.getChatRoom()))
                .collect(Collectors.toList());
    }

    @Transactional
    public ChatRoom createChatRoom(String chatRoomName, Member creator) {
        ChatRoom chatRoom = ChatRoom.builder()
                .name(chatRoomName)
                .build();
        chatRoomRepository.saveAndFlush(chatRoom);
        log.info("채팅방 저장 후 ID 확인: {}", chatRoom.getId());

        ChatRoom savedRoom = chatRoomRepository.findById(chatRoom.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.CHATROOM_SAVE_FAILED));
        log.info("채팅방 생성됨 - ID: {}, Name: {}", savedRoom.getId(), savedRoom.getName());

        ChatPart chatPart = new ChatPart(
                new ChatPartId(creator.getId(), chatRoom.getId()),
                creator,
                chatRoom,
                ChatPart.Authority.OWNER
        );
        chatPartRepository.save(chatPart);
        chatPartRepository.flush();
        log.info("채팅방 생성자 추가됨 - MemberID: {}, RoomID: {}", creator.getId(), savedRoom.getId());

        redisChatRoomRepository.saveChatRoom(savedRoom);
        log.info("채팅방 Redis 저장 완료 - ID: {}", savedRoom.getId());

        return savedRoom;
    }

    @Transactional
    public void leaveChatRoom(String roomId, Member member) {
        ChatRoom chatRoom = findChatRoomById(roomId);
        ChatPart chatPart = chatPartRepository.findByMemberAndChatRoom(member, chatRoom);

        if (chatPart.getAuthority() == ChatPart.Authority.OWNER) {
            chatPartRepository.deleteByChatRoom(chatRoom);
            chatRoomRepository.delete(chatRoom);
            redisChatRoomRepository.deleteChatRoom(chatRoom.getId());
            log.info("채팅방 삭제 완료 - Room ID: {}", roomId);
        } else {
            chatPartRepository.delete(chatPart);
            log.info("사용자 채팅방 나가기 - Member ID: {}, Room ID: {}", member.getId(), roomId);
        }
    }

    private ChatRoom findChatRoomById(String roomId) {
        return chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new CustomException(ErrorCode.CHATROOM_NOT_FOUND));
    }

    private Member findMemberById(String memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_USER));
    }

    private Member getChatRoomOwner(String roomId) {
        ChatPart ownerPart = chatPartRepository.findByChatRoom_IdAndAuthority(roomId, ChatPart.Authority.OWNER)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_USER));

        return ownerPart.getMember();
    }
}