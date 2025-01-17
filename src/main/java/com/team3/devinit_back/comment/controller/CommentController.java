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
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/comment")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;
    private final BoardService boardService;
    private final MemberService memberService;


    //댓글 작성
    @PostMapping
    public ResponseEntity<CommentResponseDto> createComment(@AuthenticationPrincipal CustomOAuth2User userInfo,
                                                            @RequestBody CommentRequestDto commentRequestDto){
        Member member =  getMemberFromUserInfo(userInfo);
        CommentResponseDto commentResponseDto = commentService.createComment(member, commentRequestDto);

        return  ResponseEntity.status(HttpStatus.CREATED).body(commentResponseDto);
    }

    //대댓글 조회
    @GetMapping("/{id}")
    public ResponseEntity<List<CommentResponseDto>> getRecomments(@PathVariable("id") Long id){
        List<CommentResponseDto> commentResponseDto = commentService.getRecommentById(id);
        return ResponseEntity.ok(commentResponseDto);
    }

    //댓글 수정
    @PatchMapping("/{id}")
    public ResponseEntity<CommentResponseDto> updateComment(@AuthenticationPrincipal CustomOAuth2User userInfo,
                                                            @RequestBody CommentRequestDto commentRequestDto,
                                                            @PathVariable("id") Long id) throws AccessDeniedException {
        Member member = getMemberFromUserInfo(userInfo);
        commentService.updateComment(member.getId(), commentRequestDto, id);
        return  ResponseEntity.status(HttpStatus.OK).build();

    }

    //댓글 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@AuthenticationPrincipal CustomOAuth2User userInfo,
                                              @RequestBody CommentRequestDto commentRequestDto,
                                              @PathVariable("id") Long id) throws AccessDeniedException {
        Member member = getMemberFromUserInfo(userInfo);
        commentService.deleteComment(member.getId(), commentRequestDto, id);
        return ResponseEntity.ok().build();
    }


    private Member getMemberFromUserInfo(CustomOAuth2User userInfo) {
        String socialId = userInfo.getName();
        return memberService.findMemberBySocialId(socialId);
    }
}
