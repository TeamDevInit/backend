package com.team3.devinit_back.board.controller;

import com.team3.devinit_back.board.dto.BoardRequestDto;
import com.team3.devinit_back.board.dto.BoardDetailResponseDto;
import com.team3.devinit_back.board.dto.BoardResponseDto;
import com.team3.devinit_back.board.service.BoardService;
import com.team3.devinit_back.member.dto.CustomOAuth2User;
import com.team3.devinit_back.member.entity.Member;
import com.team3.devinit_back.member.service.MemberService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/board")
@RequiredArgsConstructor

public class BoardController {

    private final MemberService memberService;
    private final BoardService boardService;

    //게시글 생성
    @PostMapping
    public ResponseEntity<BoardDetailResponseDto> createBoard(@AuthenticationPrincipal CustomOAuth2User userInfo,
                                                              @RequestBody BoardRequestDto boardRequestDto) {
        Member member = getMemberFromUserInfo(userInfo);
        BoardDetailResponseDto boardResponseDto = boardService.createBoard(member, boardRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(boardResponseDto);
    }

    // 전체 게시글 조회
    @GetMapping
    public ResponseEntity<Page<BoardResponseDto>> getAllBoards(@PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable){
        Page<BoardResponseDto> boardResponseDtoPage = boardService.getAllBoard(pageable);
        return  ResponseEntity.ok(boardResponseDtoPage);

    }

    // 카테고리별 게시글 조회
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<Page<BoardResponseDto>> getBoardsByCategory(@PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
                                                                            @PathVariable("categoryId") Long categoryId) {

        Page<BoardResponseDto> boardResponseDtoPage = boardService.getBoardByCategory(pageable, categoryId);
        return  ResponseEntity.ok(boardResponseDtoPage);
    }

    //게시글 상세 조회
    @GetMapping("/{id}")
    public ResponseEntity<BoardDetailResponseDto> getBoardDetail(@PathVariable("id") Long id){
        BoardDetailResponseDto boardResponseDto = boardService.getBoardDetail(id);
        return ResponseEntity.ok(boardResponseDto);
    }


    // 게시글 수정
    @PatchMapping("/{id}")
    public ResponseEntity<BoardDetailResponseDto> updateBoard(@AuthenticationPrincipal CustomOAuth2User userInfo,
                                                              @RequestBody BoardRequestDto boardRequestDto,
                                                              @PathVariable("id") Long id){

        Member member = getMemberFromUserInfo(userInfo);
        try{
            boardService.updateBoard(member.getId(),id, boardRequestDto);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    //게시글 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBoard(@AuthenticationPrincipal CustomOAuth2User userInfo,
                                      @PathVariable("id") Long id) {
        Member member = getMemberFromUserInfo(userInfo);
        try{
            boardService.deleteBoard(id,member.getId());
            return ResponseEntity.ok().build();
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    //게시글 추천
    @PostMapping("/{id}/recommendation")
    public ResponseEntity<?> recommendBoard(@AuthenticationPrincipal CustomOAuth2User userInfo,
                                            @PathVariable("id") Long id){
        Member member = getMemberFromUserInfo(userInfo);

        try{
            boolean recommended  = boardService.toggleRecommend(id, member);
            int recommendationCnt = boardService.getRecommendationCount(id);

            String message = recommended ? "게시글을 추천했습니다." : "게시글 추천을 취소했습니다.";
            Map<String, Object> response = new HashMap<>();
            response.put("message", message);
            response.put("recommendationCnt", recommendationCnt);

            return ResponseEntity.ok(response); // 200 OK 응답

        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("게시글을 찾을 수 없습니다.");
        }
    }

    private Member getMemberFromUserInfo(CustomOAuth2User userInfo) {
        String socialId = userInfo.getName();
        return memberService.findMemberBySocialId(socialId);
    }

}
