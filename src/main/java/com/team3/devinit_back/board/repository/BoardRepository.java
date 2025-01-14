package com.team3.devinit_back.board.repository;

import com.team3.devinit_back.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<Board,String> {

}
