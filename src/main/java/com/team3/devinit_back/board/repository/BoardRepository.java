package com.team3.devinit_back.board.repository;

import com.team3.devinit_back.board.entity.Board;
import com.team3.devinit_back.board.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<Board,Long> {

    Page<Board> findAllByCategory(Category category, Pageable pageable);

    @Query("SELECT b FROM Board b " +
            "LEFT JOIN FETCH b.comment c " +
            "WHERE b.id = :id " +
            "AND c.parentComment IS NULL")
    Optional<Board> findByIdWithComments(@Param("id") Long id);


    @Query("SELECT DISTINCT b FROM Board b " +
            "LEFT JOIN FETCH b.tagBoards tb " +
            "LEFT JOIN FETCH tb.tag " +
            "WHERE b.category = :category")
    Page<Board> findAllByCategoryWithTags(@Param("category") Category category, Pageable pageable);

    @Query("SELECT DISTINCT b FROM Board b " +
            "LEFT JOIN FETCH b.tagBoards tb " +
            "LEFT JOIN FETCH tb.tag")
    Page<Board> findAllWithTags(Pageable pageable);

    @Query("SELECT DISTINCT b FROM Board b " +
            "LEFT JOIN FETCH b.comment c " +
            "LEFT JOIN FETCH b.tagBoards tb " +
            "LEFT JOIN FETCH tb.tag " +
            "WHERE b.id = :id")
    Optional<Board> findByIdWithCommentsAndTags(@Param("id") Long id);

    @Query("SELECT b FROM Board b WHERE b.member.id = :memberId")
    Page<Board> findByMemberId(@Param("memberId") String memberId, Pageable pageable);
}