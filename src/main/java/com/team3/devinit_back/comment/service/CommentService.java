package com.team3.devinit_back.comment.service;


import com.team3.devinit_back.board.entity.Board;
import com.team3.devinit_back.board.repository.BoardRepository;
import com.team3.devinit_back.comment.dto.CommentRequestDto;
import com.team3.devinit_back.comment.dto.CommentResponseDto;
import com.team3.devinit_back.comment.entity.Comment;
import com.team3.devinit_back.comment.repository.CommentRepository;
import com.team3.devinit_back.global.exception.CustomException;
import com.team3.devinit_back.global.exception.ErrorCode;
import com.team3.devinit_back.member.entity.Member;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;

    //댓글 생성
    @Transactional
    public CommentResponseDto createComment(Member member, CommentRequestDto commentRequestDto){
        Board board = getBoardById(commentRequestDto.getBoardId());

        Comment parentComment = null;
        Long parentId = commentRequestDto.getParentCommentId();
        if(parentId != null){
            parentComment = getCommentById(parentId);
            parentComment.setCommentCnt(parentComment.getCommentCnt() + 1);
        }
        Comment comment = Comment.builder()
                .content(commentRequestDto.getContent())
                .member(member)
                .board(board)
                .parentComment(parentComment)
                .build();
        Comment savedComment = commentRepository.save(comment);

        board.setCommentCnt(board.getCommentCnt() + 1);
        boardRepository.save(board);
        return  CommentResponseDto.fromEntity(savedComment);

    }

    //댓글 수정
    @Transactional
    public void updateComment(String memberId, CommentRequestDto commentRequestDto, Long commentId){
        Comment comment = isAuthorizedForComment(commentId, memberId);
        comment.setContent(commentRequestDto.getContent());
        commentRepository.save(comment);
    }

    //댓글 삭제
    @Transactional
    public void deleteComment(String memberId, CommentRequestDto commentRequestDto, Long commentId) {
        Comment comment = isAuthorizedForComment(commentId, memberId, commentRequestDto.getBoardId());
        if (comment.getParentComment() != null) {
            Comment parentComment = comment.getParentComment();
            parentComment.setCommentCnt(parentComment.getCommentCnt() - 1);
            commentRepository.save(parentComment);
        }
        commentRepository.delete(comment);

        Board board = getBoardById(commentRequestDto.getBoardId());
        board.setCommentCnt(getCommentCount(board.getId()));
        boardRepository.save(board);
    }

    //대댓글 조회
    public List<CommentResponseDto> getRecommentById(Long id){
        Comment parentComment  = getCommentById(id);
        return commentRepository.findAllByParentComment(parentComment).stream()
                .map(CommentResponseDto::fromEntity)
                .toList();
    }

    //--헬퍼 메소드--//

    // boardId -> 게시글객체 조회
    private Board getBoardById(Long id) {
        return boardRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));
    }

    // commentId -> 댓글객체 조회
    private Comment getCommentById(Long id){
        return commentRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));
    }

    //게시글 댓글수 조회
    public  int getCommentCount(Long id){
        Board board = getBoardById(id);
        return commentRepository.countByBoard(board);
    }

    // 권한 검사(댓글 수정)
    private Comment isAuthorizedForComment(Long commentId, String memberId){
        Comment comment = getCommentById(commentId);
        if ( !comment.getMember().getId().equals(memberId)) {
            throw new CustomException(ErrorCode.ACCESS_DENIED);
        }
        return comment;
    }

    // 권한 검사(댓글 삭제)
    private Comment isAuthorizedForComment(Long commentId, String memberId, Long boardId){
        Board board = getBoardById(boardId);
        Comment comment = getCommentById(commentId);
        if (!board.getMember().getId().equals(memberId) & !comment.getMember().getId().equals(memberId)) {
            throw new CustomException(ErrorCode.ACCESS_DENIED);
        }
        return comment;
    }



}
