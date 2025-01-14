package com.team3.devinit_back.board.controller;

import com.team3.devinit_back.board.dto.BoardRequestDto;
import com.team3.devinit_back.board.dto.BoardResponseDto;
import com.team3.devinit_back.board.service.BoardService;
import com.team3.devinit_back.member.dto.CustomOAuth2User;
import com.team3.devinit_back.member.entity.Member;
import com.team3.devinit_back.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.AccessDeniedException;

@RestController
@RequestMapping("/api/board")
@RequiredArgsConstructor

public class BoardController {

    private final MemberService memberService;
    private final BoardService boardService;

    //게시글 생성
    @PostMapping
    public ResponseEntity<BoardResponseDto> createBoard(@AuthenticationPrincipal CustomOAuth2User userInfo,
                                                        @RequestBody BoardRequestDto boardRequestDto) throws IOException, IllegalAccessException {
        String socialId = userInfo.getName();
        Member member = memberService.findMemberBySocialId(socialId);
        BoardResponseDto boardResponseDto = boardService.createBoard(member, boardRequestDto);

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
                                                                      @PathVariable("categoryId") Long id) throws IllegalAccessException {

        Page<BoardResponseDto> boardResponseDtoPage = boardService.getBoardByCategory(pageable, id);
        return  ResponseEntity.ok(boardResponseDtoPage);
    }

    //게시글 상세 조회
    @GetMapping("/{id}")
    public ResponseEntity<BoardResponseDto> getBoardDetail(@PathVariable("id") Long id){
        BoardResponseDto boardResponseDto = boardService.getBoardDetail(id);
        return ResponseEntity.ok().body(boardResponseDto);
    }



    // 게시글 수정
    @PatchMapping("/{id}")
    public ResponseEntity<BoardResponseDto> updateBoard(@AuthenticationPrincipal CustomOAuth2User userInfo,
                                                        @RequestBody BoardRequestDto boardRequestDto, @PathVariable("id") Long id){

        String socialId = userInfo.getName();
        String memberId = memberService.findMemberBySocialId(socialId).getId();
        try{
            boardService.updateBoard(memberId,id, boardRequestDto);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (IllegalAccessException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    //게시글 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity deleteBoard(@AuthenticationPrincipal CustomOAuth2User userInfo,
                                      @PathVariable("id") Long id) throws AccessDeniedException {
        String socialId = userInfo.getName();
        String memberId = memberService.findMemberBySocialId(socialId).getId();
        try{
            boardService.deleteBoard(id,memberId);
            return ResponseEntity.ok().build();
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }


    }

}
