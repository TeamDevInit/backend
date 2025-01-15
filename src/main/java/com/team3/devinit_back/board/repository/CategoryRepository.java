package com.team3.devinit_back.board.repository;

import com.team3.devinit_back.board.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Long> {
    boolean existsByName(String categoryName);
}
