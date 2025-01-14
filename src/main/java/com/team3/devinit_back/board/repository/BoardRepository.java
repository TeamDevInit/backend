package com.team3.devinit_back.board.repository;

import com.team3.devinit_back.board.entity.Board;
import com.team3.devinit_back.board.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<Board,Long> {

    Page<Board> findAllByCategory(Category category, Pageable pageable);
}
