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
import org.springframework.data.redis.core.RedisTemplate;
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
    private final JWTUtil jwtUtil;
    private final MemberService memberService;
    private final ChatRoomRepository chatRoomRepository;
    private final RedisChatRoomRepository redisChatRoomRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        // WebSocket 연결 시 JWT 인증 수행
        if (StompCommand.CONNECT == accessor.getCommand()) {
            String jwt = accessor.getFirstNativeHeader("access");

            if (jwt == null) {
                log.warn("WebSocket 연결 실패: access 헤더가 없습니다.");
                throw new CustomException(ErrorCode.EMPTY_ACCESS_TOKEN);
            }

            if (jwtUtil.isExpired(jwt)) {
                log.warn("WebSocket 연결 실패: JWT가 만료되었습니다.");
                throw new CustomException(ErrorCode.EXPIRED_ACCESS_TOKEN);
            }

            String socialId = jwtUtil.getSocialId(jwt);
            Member member = memberService.findMemberBySocialId(socialId);
            if (member == null) {
                throw new CustomException(ErrorCode.UNAUTHORIZED);
            }

            String sessionId = accessor.getSessionId();
            accessor.setUser(new UsernamePasswordAuthenticationToken(socialId, null, new ArrayList<>()));
            accessor.getSessionAttributes().put("user", socialId);

            redisTemplate.opsForHash().put("USER_SESSION", sessionId, socialId);
            log.info("WebSocket 인증 성공 - 사용자: {}, 세션ID: {}", member.getNickName(), sessionId);
        }

        // 채팅방 구독 시 사용자 정보를 Redis에 저장
        if (StompCommand.SUBSCRIBE == accessor.getCommand()) {
            String destination = Optional.ofNullable((String) message.getHeaders().get("simpDestination"))
                    .orElse("InvalidRoomId");
            String roomId = extractRoomId(destination);
            String socialId = (String) accessor.getSessionAttributes().get("user");

            if (socialId == null) {
                log.warn("SUBSCRIBE 요청 시 사용자 정보 없음");
                throw new CustomException(ErrorCode.UNAUTHORIZED);
            }

            redisChatRoomRepository.saveChatRoom(chatRoomRepository.findById(roomId)
                    .orElseThrow(() -> new CustomException(ErrorCode.CHATROOM_NOT_FOUND)));

            log.info("[StompHandler] 채팅방 구독 완료 - 사용자: {}, 방 ID: {}", socialId, roomId);
        }

        if (StompCommand.DISCONNECT == accessor.getCommand()) {
            String sessionId = accessor.getSessionId();
            String socialId = (String) redisTemplate.opsForHash().get("USER_SESSION", sessionId);
            log.info("WebSocket 세션 종료 - 사용자: {}, 세션 ID: {}", socialId, sessionId);
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