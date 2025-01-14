package com.team3.devinit_back.board.repository;

import com.team3.devinit_back.board.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository  extends JpaRepository<Tag,Long> {
    Tag findByName(String name);

    boolean existsByName(String name);
}
