package com.team3.devinit_back.board.service;

import com.team3.devinit_back.board.dto.BoardRequestDto;
import com.team3.devinit_back.board.dto.BoardResponseDto;
import com.team3.devinit_back.board.entity.Board;
import com.team3.devinit_back.board.entity.Category;
import com.team3.devinit_back.board.repository.BoardRepository;
import com.team3.devinit_back.board.repository.CategoryRepository;
import com.team3.devinit_back.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final CategoryRepository categoryRepository;
    public BoardResponseDto createBoard(Member member, BoardRequestDto boardRequestDto) throws IOException, IllegalAccessException {

        Category category =  categoryRepository.findById(boardRequestDto.getCategoryId())
                .orElseThrow(() -> new IllegalAccessException("유효하지 않는 카테고리 ID"));
        Board board = Board.builder()
                .title(boardRequestDto.getTitle())
                .content(boardRequestDto.getContent())
                .member(member)
                .category(category)
                .build();

        Board savedBoard = boardRepository.save(board);
        return BoardResponseDto.fromEntity(savedBoard);

    }
}
