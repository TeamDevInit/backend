package com.team3.devinit_back.comment.controller;

import com.team3.devinit_back.board.service.BoardService;
import com.team3.devinit_back.comment.dto.CommentRequestDto;
import com.team3.devinit_back.comment.dto.CommentResponseDto;
import com.team3.devinit_back.comment.service.CommentService;
import com.team3.devinit_back.member.dto.CustomOAuth2User;
import com.team3.devinit_back.member.entity.Member;
import com.team3.devinit_back.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;
    private final BoardService boardService;
    private final MemberService memberService;

    @PostMapping
    @Operation(
            summary = "댓글 작성",
            description = "사용자가 게시물에 댓글을 작성합니다.")
    public ResponseEntity<CommentResponseDto> createComment(@AuthenticationPrincipal CustomOAuth2User userInfo,
                                                            @RequestBody CommentRequestDto commentRequestDto){
        Member member =  getMemberFromUserInfo(userInfo);
        CommentResponseDto commentResponseDto = commentService.createComment(member, commentRequestDto);

        return  ResponseEntity.status(HttpStatus.CREATED).body(commentResponseDto);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "대댓글 조회",
            description = "해당 댓글에 대한 대댓글을 모두 조회합니다.")
    public ResponseEntity<List<CommentResponseDto>> getRecomments(@PathVariable("id") Long id){
        List<CommentResponseDto> commentResponseDto = commentService.getRecommentById(id);
        return ResponseEntity.ok(commentResponseDto);
    }

    @PatchMapping("/{id}")
    @Operation(
            summary = "댓글 수정",
            description = "ID에 해당한 댓글을 작성자가 댓글을 수정합니다.")
    public ResponseEntity<Void> updateComment(@AuthenticationPrincipal CustomOAuth2User userInfo,
                                                            @RequestBody CommentRequestDto commentRequestDto,
                                                            @PathVariable("id") Long id){
        Member member = getMemberFromUserInfo(userInfo);
        commentService.updateComment(member.getId(), commentRequestDto, id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "댓글 삭제",
            description = "ID에 해당한 댓글을 작성자 혹은 게시물 작성자가 삭제합니다.")
    public ResponseEntity<Void> deleteComment(@AuthenticationPrincipal CustomOAuth2User userInfo,
                                              @RequestBody CommentRequestDto commentRequestDto,
                                              @PathVariable("id") Long id){
        Member member = getMemberFromUserInfo(userInfo);
        commentService.deleteComment(member.getId(), commentRequestDto, id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    private Member getMemberFromUserInfo(CustomOAuth2User userInfo) {
        String socialId = userInfo.getName();
        return memberService.findMemberBySocialId(socialId);
    }
}
