package com.team3.devinit_back.board.controller;

import com.team3.devinit_back.board.dto.BoardRequestDto;
import com.team3.devinit_back.board.dto.BoardDetailResponseDto;
import com.team3.devinit_back.board.dto.BoardResponseDto;
import com.team3.devinit_back.board.service.BoardService;
import com.team3.devinit_back.member.dto.CustomOAuth2User;
import com.team3.devinit_back.member.entity.Member;
import com.team3.devinit_back.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/boards")
@RequiredArgsConstructor

public class BoardController {

    private final MemberService memberService;
    private final BoardService boardService;

    @PostMapping
    public ResponseEntity<BoardDetailResponseDto> createBoard(@AuthenticationPrincipal CustomOAuth2User userInfo,
                                                              @RequestBody BoardRequestDto boardRequestDto) {
        Member member = getMemberFromUserInfo(userInfo);
        BoardDetailResponseDto boardResponseDto = boardService.createBoard(member, boardRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(boardResponseDto);
    }


    @GetMapping
    @Operation(summary = "게시글 전체 조회", description = "페이지 번호/사이즈/정렬조건/태그/검색어를 입력받아 해당 페이지게시글을 조회한다.")
    public ResponseEntity<Page<BoardResponseDto>> getAllBoards(@PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
                                                               @Parameter(description = "검색하는 태그값") @RequestParam(name = "tagNames", required = false)List<String> tagNames,
                                                               @Parameter(description = "검색하는 내용값") @RequestParam(name = "searchContents",required = false) String searchContents){
        Page<BoardResponseDto> boardResponseDtoPage = boardService.getAllBoard(pageable, tagNames, searchContents);
        return  ResponseEntity.ok(boardResponseDtoPage);
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<Page<BoardResponseDto>> getBoardsByCategory(@PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
                                                                          @RequestParam(name = "tagNames", required = false)List<String> tagNames,
                                                                          @RequestParam(name = "searchContents",required = false) String searchContents,
                                                                          @PathVariable("categoryId") Long categoryId) {

        Page<BoardResponseDto> boardResponseDtoPage = boardService.getBoardByCategory(pageable, tagNames, searchContents, categoryId);
        return  ResponseEntity.ok(boardResponseDtoPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BoardDetailResponseDto> getBoardDetail(@AuthenticationPrincipal CustomOAuth2User userInfo,
                                                                 @PathVariable("id") Long id){
        Member member =  null;
        if(userInfo!=null){
            member = getMemberFromUserInfo(userInfo);
        }
        BoardDetailResponseDto boardResponseDto = boardService.getBoardDetail(id, member);
        return ResponseEntity.ok(boardResponseDto);
    }


    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateBoard(@AuthenticationPrincipal CustomOAuth2User userInfo,
                                            @RequestBody BoardRequestDto boardRequestDto,
                                            @PathVariable("id") Long id){

        Member member = getMemberFromUserInfo(userInfo);

        boardService.updateBoard(member.getId(),id, boardRequestDto);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBoard(@AuthenticationPrincipal CustomOAuth2User userInfo,
                                            @PathVariable("id") Long id) {
        Member member = getMemberFromUserInfo(userInfo);

        boardService.deleteBoard(id,member.getId());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }

    @PostMapping("/{id}/recommendation")
    public ResponseEntity<?> recommendBoard(@AuthenticationPrincipal CustomOAuth2User userInfo,
                                            @PathVariable("id") Long id){
        Member member = getMemberFromUserInfo(userInfo);


            boolean recommended  = boardService.toggleRecommend(id, member);
            int recommendationCnt = boardService.getRecommendationCount(id);

            String message = recommended ? "게시글을 추천했습니다." : "게시글 추천을 취소했습니다.";
            Map<String, Object> response = new HashMap<>();
            response.put("message", message);
            response.put("recommendationCnt", recommendationCnt);

            return ResponseEntity.ok(response);


    }

    private Member getMemberFromUserInfo(CustomOAuth2User userInfo) {
        String socialId = userInfo.getName();
        return memberService.findMemberBySocialId(socialId);
    }

}
