package com.team3.devinit_back.comment.controller;

import com.team3.devinit_back.board.service.BoardService;
import com.team3.devinit_back.comment.dto.CommentRequestDto;
import com.team3.devinit_back.comment.dto.CommentResponseDto;
import com.team3.devinit_back.comment.service.CommentService;
import com.team3.devinit_back.member.dto.CustomOAuth2User;
import com.team3.devinit_back.member.entity.Member;
import com.team3.devinit_back.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/comment")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;
    private final BoardService boardService;
    private final MemberService memberService;


    @PostMapping
    public ResponseEntity<CommentResponseDto> createComment(@AuthenticationPrincipal CustomOAuth2User userInfo,
                                                            @RequestBody CommentRequestDto commentRequestDto){
        Member member =  getMemberFromUserInfo(userInfo);
        CommentResponseDto commentResponseDto = commentService.createComment(member, commentRequestDto);

        return  ResponseEntity.status(HttpStatus.CREATED).body(commentResponseDto);
    }




    private Member getMemberFromUserInfo(CustomOAuth2User userInfo) {
        String socialId = userInfo.getName();
        return memberService.findMemberBySocialId(socialId);
    }
}
