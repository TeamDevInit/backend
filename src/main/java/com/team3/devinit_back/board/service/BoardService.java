package com.team3.devinit_back.board.service;

import com.team3.devinit_back.board.dto.BoardRequestDto;
import com.team3.devinit_back.board.dto.BoardResponseDto;
import com.team3.devinit_back.board.entity.Board;
import com.team3.devinit_back.board.entity.Category;
import com.team3.devinit_back.board.entity.Tag;
import com.team3.devinit_back.board.entity.TagBoard;
import com.team3.devinit_back.board.repository.BoardRepository;
import com.team3.devinit_back.board.repository.CategoryRepository;
import com.team3.devinit_back.member.entity.Member;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.AccessDeniedException;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final CategoryRepository categoryRepository;
    private final TagService tagService;

    // 게시글 생성
    @Transactional
    public BoardResponseDto createBoard(Member member, BoardRequestDto boardRequestDto) throws IOException, IllegalAccessException {

        Category category =  categoryRepository.findById(boardRequestDto.getCategoryId())
                .orElseThrow(() -> new IllegalAccessException("유효하지 않는 카테고리 ID"));
        Board board = Board.builder()
                .title(boardRequestDto.getTitle())
                .content(boardRequestDto.getContent())
                .member(member)
                .category(category)
                .build();
        if(boardRequestDto.getTags() != null){
            for(String tagName : boardRequestDto.getTags()){
                Tag tag = tagService.findTag(tagName);
                TagBoard tagBoard = new TagBoard(board, tag);
                board.getTagBoards().add(tagBoard);
            }
        }

        Board savedBoard = boardRepository.save(board);
        return BoardResponseDto.fromEntity(savedBoard);

    }

    // 전체 게시물 조회
    public Page<BoardResponseDto> getAllBoard(Pageable pageable){
        return boardRepository.findAll(pageable)
                .map(BoardResponseDto::fromEntity);
    }

    // 카테고리별 게시물 조회
    public Page<BoardResponseDto> getBoardByCategory(Pageable pageable, Long categoryId) throws IllegalAccessException {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalAccessException("유효하지 않는 카테고리 ID"));
        return boardRepository.findAllByCategory(category, pageable)
                .map(BoardResponseDto::fromEntity);


    }

    //게시물 상세 조회
    public BoardResponseDto getBoardDetail(Long id){
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("찾을 수 없는 Board Id: " + id));
        return BoardResponseDto.fromEntity(board);
    }

    // 게시글 수정
    @Transactional
    public void updateBoard(String memberId, Long id,BoardRequestDto boardRequestDto) throws AccessDeniedException, IllegalAccessException {
        Category category =  categoryRepository.findById(boardRequestDto.getCategoryId())
                .orElseThrow(() -> new IllegalAccessException("유효하지 않는 카테고리 ID"));
        Board board = isAuthorized(id, memberId);
        board.setTitle(boardRequestDto.getTitle());
        board.setContent(boardRequestDto.getContent());
        board.setCategory(category);

        board.getTagBoards().clear();
        if (boardRequestDto.getTags() != null) {
            for (String tagName : boardRequestDto.getTags()) {
                Tag tag = tagService.findTag(tagName);
                TagBoard tagBoard = new TagBoard(board, tag);
                board.getTagBoards().add(tagBoard);
            }
        }

        boardRepository.save(board);
    }

    //게시글 삭제
    @Transactional
    public void deleteBoard(Long id, String memberId) throws AccessDeniedException {
        Board board = isAuthorized(id, memberId);
        boardRepository.deleteById(board.getId());
    }


    // 권한 검사
    private Board isAuthorized(Long id, String memberId) throws AccessDeniedException {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("찾을 수 없는 Board Id: " + id));
        if (!board.getMember().getId().equals(memberId)) {
            throw new AccessDeniedException("해당 게시물에 대한 권한이 없습니다.");
        }
        return board;
    }
}
