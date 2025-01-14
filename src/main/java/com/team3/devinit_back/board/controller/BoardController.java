package com.team3.devinit_back.board.controller;

import com.team3.devinit_back.board.dto.BoardRequestDto;
import com.team3.devinit_back.board.dto.BoardResponseDto;
import com.team3.devinit_back.board.service.BoardService;
import com.team3.devinit_back.member.dto.CustomOAuth2User;
import com.team3.devinit_back.member.entity.Member;
import com.team3.devinit_back.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/board")
@RequiredArgsConstructor

public class BoardController {

    private final MemberService memberService;
    private final BoardService boardService;

    @PostMapping
    public ResponseEntity<BoardResponseDto> createBoard(@AuthenticationPrincipal CustomOAuth2User userInfo,
                                                        @RequestBody BoardRequestDto boardRequestDto) throws IOException, IllegalAccessException {
        String socialId = userInfo.getName();
        Member member = memberService.findMemberBySocialId(socialId);
        BoardResponseDto boardResponseDto = boardService.createBoard(member, boardRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(boardResponseDto);
    }

}
