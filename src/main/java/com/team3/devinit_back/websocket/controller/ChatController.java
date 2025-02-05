package com.team3.devinit_back.websocket.controller;

import com.team3.devinit_back.member.entity.Member;
import com.team3.devinit_back.member.jwt.JWTUtil;
import com.team3.devinit_back.member.service.MemberService;
import com.team3.devinit_back.websocket.dto.ChatMessageDto;
import com.team3.devinit_back.websocket.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ChatController {
    private final ChatService chatService;
    private final MemberService memberService;
    private final JWTUtil jwtUtil;

    @MessageMapping("/chat/message")
    public void message(@Payload ChatMessageDto message, @Header("access") String token) {
        if (token == null) {
            log.error("인증되지 않은 사용자입니다.");
            return;
        }

        String socialId = jwtUtil.getSocialId(token);
        Member sender = memberService.findMemberBySocialId(socialId);

        message.setSender(socialId);
        message.setNickname(sender.getNickName());

        chatService.processMessage(message);
    }
}