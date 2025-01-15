package com.team3.devinit_back.board.service;

import com.team3.devinit_back.board.dto.BoardRequestDto;
import com.team3.devinit_back.board.dto.BoardResponseDto;
import com.team3.devinit_back.board.entity.*;
import com.team3.devinit_back.board.repository.BoardRepository;
import com.team3.devinit_back.board.repository.CategoryRepository;
import com.team3.devinit_back.board.repository.RecommendationRepository;
import com.team3.devinit_back.member.entity.Member;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final CategoryRepository categoryRepository;
    private final RecommendationRepository recommendationRepository;
    private final TagService tagService;

    // 게시글 생성
    @Transactional
    public BoardResponseDto createBoard(Member member, BoardRequestDto boardRequestDto) {

        Category category = getCategoryById(boardRequestDto.getCategoryId());
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
    public Page<BoardResponseDto> getBoardByCategory(Pageable pageable, Long categoryId) {
        Category category = getCategoryById(categoryId);
        return boardRepository.findAllByCategory(category, pageable)
                .map(BoardResponseDto::fromEntity);


    }

    //게시물 상세 조회
    public BoardResponseDto getBoardDetail(Long id){
        Board board = getBoardById(id);
        return BoardResponseDto.fromEntity(board);
    }

    // 게시글 수정
    @Transactional
    public void updateBoard(String memberId, Long id,BoardRequestDto boardRequestDto) throws AccessDeniedException {
        Category category = getCategoryById(boardRequestDto.getCategoryId());
        Board board = isAuthorized(id, memberId);
        board.setTitle(boardRequestDto.getTitle());
        board.setContent(boardRequestDto.getContent());
        board.setCategory(category);

        if (boardRequestDto.getTags() != null) {
            board.getTagBoards().clear();
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

    // 게시글 추천 토글
    @Transactional
    public boolean toggleRecommend(Long id, Member member) {
        Board board = getBoardById(id);
        Optional<Recommendation> recommendation = recommendationRepository.findByBoardAndMember(board, member);
        if(recommendation.isPresent()){
            recommendationRepository.delete(recommendation.get());
            board.setUpCnt(board.getUpCnt() - 1);
            boardRepository.save(board);
            return false;
        }else{
            Recommendation newRecommendation = Recommendation.builder()
                    .board(board)
                    .member(member)
                    .build();
            recommendationRepository.save(newRecommendation);
            board.setUpCnt(board.getUpCnt() + 1);
            boardRepository.save(board);
            return true;
        }
    }

    // 게시글 추천수 조회
    public  int getRecommendationCount(Long id){
        Board board = getBoardById(id);
        return recommendationRepository.countByBoard(board);
    }


    // boardId -> 게시글객체 조회
    private Board getBoardById(Long id) {
        return boardRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("ID에 해당하는 게시물을 찾을 수 없습니다." + id));
    }

    //CategoryId -> 카테고리객체 조회
    private Category getCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("ID에 해당하는 카테고리를 찾을 수 없습니다." + categoryId));
    }

    // 권한 검사
    private Board isAuthorized(Long id, String memberId) throws AccessDeniedException {
        Board board = getBoardById(id);
        if (!board.getMember().getId().equals(memberId)) {
            throw new AccessDeniedException("해당 게시물에 대한 권한이 없습니다.");
        }
        return board;
    }
}
