package com.team3.devinit_back.websocket.controller;

import com.team3.devinit_back.global.exception.CustomException;
import com.team3.devinit_back.global.exception.ErrorCode;
import com.team3.devinit_back.member.dto.CustomOAuth2User;
import com.team3.devinit_back.member.entity.Member;
import com.team3.devinit_back.member.service.MemberService;
import com.team3.devinit_back.websocket.dto.ChatRoomDetailDto;
import com.team3.devinit_back.websocket.dto.ChatRoomListDto;
import com.team3.devinit_back.websocket.dto.ChatRoomRequestDto;
import com.team3.devinit_back.websocket.entity.ChatRoom;
import com.team3.devinit_back.websocket.service.ChatRoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "ChatRoom API", description = "채팅방 관리 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/chat")
public class ChatRoomController {
    private final ChatRoomService chatRoomService;
    private final MemberService memberService;

    @Operation(summary = "전체 채팅방 목록 조회", description = "서버에 존재하는 모든 채팅방 목록을 조회합니다.")
    @GetMapping("/rooms")
    public List<ChatRoomListDto> getAllRooms() {
        return chatRoomService.getAllChatRooms();
    }

    @Operation(summary = "내 채팅방 목록 조회", description = "로그인한 사용자가 참여 중인 채팅방 목록을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공적으로 채팅방 목록을 조회함"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자")
    })
    @GetMapping("/my-rooms")
    public ResponseEntity<List<ChatRoomListDto>> getMyRooms(
            @AuthenticationPrincipal @Parameter(description = "OAuth2 인증 사용자 정보") CustomOAuth2User userInfo) {
        Member member = getMemberFromUserInfo(userInfo);
        List<ChatRoomListDto> myChatRooms = chatRoomService.getMyChatRooms(member);
        return ResponseEntity.ok(myChatRooms);
    }

    @Operation(summary = "채팅방 상세 조회", description = "채팅방 ID를 이용해 특정 채팅방 정보를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공적으로 채팅방 정보를 조회함"),
            @ApiResponse(responseCode = "404", description = "해당 ID의 채팅방을 찾을 수 없음")
    })
    @GetMapping("/room/{roomId}")
    public ResponseEntity<ChatRoomDetailDto> getRoom(
            @AuthenticationPrincipal @Parameter(description = "OAuth2 인증 사용자 정보") CustomOAuth2User userInfo,
            @PathVariable @Parameter(description = "조회할 채팅방 ID") String roomId) {
        Member member = getMemberFromUserInfo(userInfo);
        ChatRoomDetailDto chatRoomDetail = chatRoomService.getChatRoomById(roomId, member);
        return ResponseEntity.ok(chatRoomDetail);
    }

    @Operation(summary = "채팅방 생성", description = "새로운 채팅방을 생성합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "채팅방 생성 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자")
    })
    @PostMapping("/room")
    public ResponseEntity<Map<String, String>> createRoom(
            @AuthenticationPrincipal @Parameter(description = "OAuth2 인증 사용자 정보") CustomOAuth2User userInfo,
            @RequestBody @Parameter(description = "채팅방 생성 요청 데이터") ChatRoomRequestDto request) {
        Member member = getMemberFromUserInfo(userInfo);
        ChatRoom chatRoom = chatRoomService.createChatRoom(request.getName(), member);

        Map<String, String> response = new HashMap<>();
        response.put("roomId", chatRoom.getId());

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "채팅방 나가기", description = "사용자가 특정 채팅방을 나갑니다. 방장이 나가면 채팅방이 삭제됩니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "채팅방 나가기 성공"),
            @ApiResponse(responseCode = "404", description = "채팅방을 찾을 수 없음")
    })
    @DeleteMapping("/room/{roomId}")
    public ResponseEntity<Void> leaveChatRoom(
            @AuthenticationPrincipal @Parameter(description = "OAuth2 인증 사용자 정보") CustomOAuth2User userInfo,
            @PathVariable @Parameter(description = "나갈 채팅방 ID") String roomId) {
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