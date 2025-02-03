package com.team3.devinit_back.websocket.service;

import com.team3.devinit_back.global.exception.CustomException;
import com.team3.devinit_back.global.exception.ErrorCode;
import com.team3.devinit_back.member.entity.Member;
import com.team3.devinit_back.member.repository.MemberRepository;
import com.team3.devinit_back.websocket.dto.ChatMessageDto;
import com.team3.devinit_back.websocket.entity.ChatMessage;
import com.team3.devinit_back.websocket.entity.ChatRoom;
import com.team3.devinit_back.websocket.repository.ChatMessageRepository;
import com.team3.devinit_back.websocket.repository.ChatRoomRepository;
import com.team3.devinit_back.websocket.repository.RedisChatMessageRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatBatchService {
    private final RedisChatMessageRepository redisChatMessageRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final MemberRepository memberRepository;

    @Scheduled(fixedRate = 60000) // 1분마다 실행
    @Transactional
    public void saveMessagesToDB() {
        log.info("[ChatBatchService] 배치 저장 시작");

        List<ChatRoom> chatRooms = chatRoomRepository.findAll();
        for (ChatRoom chatRoom : chatRooms) {
            List<ChatMessageDto> messages = redisChatMessageRepository.getMessages(chatRoom.getId());
            if (!messages.isEmpty()) {
                List<ChatMessage> entities = messages.stream()
                        .map(dto -> ChatMessage.builder()
                                .chatRoom(chatRoom)
                                .sender(findMemberById(dto.getSender()))
                                .msgType(ChatMessage.MessageType.valueOf(dto.getType().name()))
                                .content(dto.getMessage())
                                .build())
                        .collect(Collectors.toList());

                chatMessageRepository.saveAll(entities);
                redisChatMessageRepository.clearMessages(chatRoom.getId());
                log.info("채팅방 ID {} - {}개 메시지 저장 완료", chatRoom.getId(), messages.size());
            }
        }
    }

    private Member findMemberById(String memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_USER));
    }
}