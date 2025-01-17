package com.team3.devinit_back.comment.repository;

import com.team3.devinit_back.board.entity.Board;
import com.team3.devinit_back.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment,Long> {
    int countByBoard(Board board);

    List<Comment> findAllByParentComment(Comment parentComment);
}
