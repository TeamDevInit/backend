package com.team3.devinit_back.comment.service;


import com.team3.devinit_back.board.entity.Board;
import com.team3.devinit_back.board.repository.BoardRepository;
import com.team3.devinit_back.comment.dto.CommentRequestDto;
import com.team3.devinit_back.comment.dto.CommentResponseDto;
import com.team3.devinit_back.comment.entity.Comment;
import com.team3.devinit_back.comment.repository.CommentRepository;
import com.team3.devinit_back.member.entity.Member;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public CommentResponseDto createComment(Member member, CommentRequestDto commentRequestDto){
        Board board = getBoardById(commentRequestDto.getBoardId());

        Comment parentComment = null;
        Long parentId = commentRequestDto.getParentCommentId();
        if(parentId != null){
            parentComment = commentRepository.findById(parentId)
                    .orElseThrow(() -> new EntityNotFoundException("부모 댓글을 찾을 수 없습니다."));
        }
        Comment comment = Comment.builder()
                .content(commentRequestDto.getContent())
                .member(member)
                .board(board)
                .build();
        Comment savedComment = commentRepository.save(comment);
        return  CommentResponseDto.fromEntity(savedComment);

    }







    // boardId -> 게시글객체 조회
    private Board getBoardById(Long id) {
        return boardRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("ID에 해당하는 게시물을 찾을 수 없습니다." + id));
    }
    private Board isAuthorized(Long id, String memberId) throws AccessDeniedException {
        Board board = getBoardById(id);
        if (!board.getMember().getId().equals(memberId)) {
            throw new AccessDeniedException("해당 게시물에 대한 권한이 없습니다.");
        }
        return board;
    }
}
