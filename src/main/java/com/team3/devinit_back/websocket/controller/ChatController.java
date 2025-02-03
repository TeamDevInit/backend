package com.team3.devinit_back.websocket.controller;

import com.team3.devinit_back.global.exception.CustomException;
import com.team3.devinit_back.member.dto.CustomOAuth2User;
import com.team3.devinit_back.member.entity.Member;
import com.team3.devinit_back.member.service.MemberService;
import com.team3.devinit_back.websocket.dto.ChatMessageDto;
import com.team3.devinit_back.websocket.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ChatController {
    private final ChatService chatService;

    @MessageMapping("/chat/message")
    public void message(@Payload ChatMessageDto message, Principal principal) {
        if (principal == null) {
            log.error("인증되지 않은 사용자입니다.");
            return;
        }

        log.info("ChatController - 현재 사용자: {}", principal.getName());

        message.setSender(principal.getName());
        chatService.processMessage(message);
    }
}