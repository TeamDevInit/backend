package com.team3.devinit_back.websocket.handler;

import com.team3.devinit_back.global.exception.CustomException;
import com.team3.devinit_back.global.exception.ErrorCode;
import com.team3.devinit_back.member.entity.Member;
import com.team3.devinit_back.member.jwt.JWTUtil;
import com.team3.devinit_back.member.service.MemberService;
import com.team3.devinit_back.websocket.repository.ChatRoomRepository;
import com.team3.devinit_back.websocket.repository.RedisChatRoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Component
public class StompHandler implements ChannelInterceptor {
    private static final int JWT_PREFIX_LENGTH = 7;
    private final JWTUtil jwtUtil;
    private final MemberService memberService;
    private final ChatRoomRepository chatRoomRepository;
    private final RedisChatRoomRepository redisChatRoomRepository;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        if (StompCommand.CONNECT == accessor.getCommand()) {
            String jwt = accessor.getFirstNativeHeader("Authorization");

            if (jwt == null || !jwt.startsWith("Bearer ")) {
                log.warn("WebSocket 연결 실패: Authorization 헤더가 없습니다.");
                throw new CustomException(ErrorCode.EMPTY_ACCESS_TOKEN);
            }

            String token = jwt.substring(JWT_PREFIX_LENGTH);
            if (jwtUtil.isExpired(token)) {
                log.warn("WebSocket 연결 실패: JWT가 만료되었습니다.");
                throw new CustomException(ErrorCode.EXPIRED_ACCESS_TOKEN);
            }

            String socialId = jwtUtil.getSocialId(token);
            Member member = memberService.findMemberBySocialId(socialId);
            if (member == null) {
                throw new CustomException(ErrorCode.UNAUTHORIZED);
            }

            accessor.setUser(new UsernamePasswordAuthenticationToken(socialId, null, new ArrayList<>()));
            log.info("WebSocket 인증 성공 - 사용자: {}", member.getNickName());
        }

        if (StompCommand.SUBSCRIBE == accessor.getCommand()) {
            String destination = Optional.ofNullable((String) message.getHeaders().get("simpDestination"))
                    .orElse("InvalidRoomId");
            String roomId = extractRoomId(destination);

            String jwt = accessor.getFirstNativeHeader("Authorization");
            String accessToken = jwt.substring(JWT_PREFIX_LENGTH);
            String socialId = jwtUtil.getSocialId(accessToken);

            redisChatRoomRepository.saveChatRoom(chatRoomRepository.findById(roomId)
                    .orElseThrow(() -> new CustomException(ErrorCode.CHATROOM_NOT_FOUND)));

            log.info("사용자({})가 채팅방({})에 입장했습니다.", socialId, roomId);
        }

        if (StompCommand.DISCONNECT == accessor.getCommand()) {
            String sessionId = accessor.getSessionId();
            log.info("WebSocket 세션 종료: {}", sessionId);
        }

        return message;
    }

    private String extractRoomId(String destination) {
        if (destination.startsWith("/sub/chat/room/")) {
            return destination.replace("/sub/chat/room/", "");
        }
        return "InvalidRoomId";
    }
}