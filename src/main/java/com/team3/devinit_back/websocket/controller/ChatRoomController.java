package com.team3.devinit_back.websocket.controller;

import com.team3.devinit_back.global.exception.CustomException;
import com.team3.devinit_back.global.exception.ErrorCode;
import com.team3.devinit_back.member.dto.CustomOAuth2User;
import com.team3.devinit_back.member.entity.Member;
import com.team3.devinit_back.member.service.MemberService;
import com.team3.devinit_back.websocket.dto.ChatRoomDetailDto;
import com.team3.devinit_back.websocket.dto.ChatRoomListDto;
import com.team3.devinit_back.websocket.entity.ChatRoom;
import com.team3.devinit_back.websocket.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/chat")
public class ChatRoomController {
    private final ChatRoomService chatRoomService;
    private final MemberService memberService;

    @GetMapping("/rooms")
    public List<ChatRoomListDto> getAllRooms() {
        return chatRoomService.getAllChatRooms();
    }

    @GetMapping("/my-rooms")
    public ResponseEntity<List<ChatRoomListDto>> getMyRooms(@AuthenticationPrincipal CustomOAuth2User userInfo) {
        Member member = getMemberFromUserInfo(userInfo);
        List<ChatRoomListDto> myChatRooms = chatRoomService.getMyChatRooms(member);
        return ResponseEntity.ok(myChatRooms);
    }

    @GetMapping("/room/{roomId}")
    public ResponseEntity<ChatRoomDetailDto> getRoom(
            @AuthenticationPrincipal CustomOAuth2User userInfo,
            @PathVariable String roomId) {
        Member member = getMemberFromUserInfo(userInfo);
        ChatRoomDetailDto chatRoomDetail = chatRoomService.getChatRoomById(roomId, member);
        return ResponseEntity.ok(chatRoomDetail);
    }

    @PostMapping("/room")
    public ResponseEntity<Map<String, String>> createRoom(
            @AuthenticationPrincipal CustomOAuth2User userInfo,
            @RequestBody Map<String, String> request) {
        Member member = getMemberFromUserInfo(userInfo);
        String name = request.get("name");

        ChatRoom chatRoom = chatRoomService.createChatRoom(name, member);

        Map<String, String> response = new HashMap<>();
        response.put("roomId", chatRoom.getId());

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/room/{roomId}")
    public ResponseEntity<Void> leaveChatRoom(@AuthenticationPrincipal CustomOAuth2User userInfo,
                                              @PathVariable String roomId) {
        Member member = getMemberFromUserInfo(userInfo);
        chatRoomService.leaveChatRoom(roomId, member);
        return ResponseEntity.noContent().build();
    }

    private Member getMemberFromUserInfo(CustomOAuth2User userInfo) {
        if (userInfo == null) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        String socialId = userInfo.getName();
        return memberService.findMemberBySocialId(socialId);
    }
}