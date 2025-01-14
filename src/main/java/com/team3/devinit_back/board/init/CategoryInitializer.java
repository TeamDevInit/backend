package com.team3.devinit_back.board.init;

import com.team3.devinit_back.board.entity.Category;
import com.team3.devinit_back.board.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CategoryInitializer implements CommandLineRunner {

    private final CategoryRepository categoryRepository;

    @Override
    public void run(String... args) throws Exception {
        List<String> defaultCategories = List.of("자유게시판", "코딩 질문", "스터디 구인",
                "프로젝트 구인", "경험 공유");


        for (String categoryName : defaultCategories) {
            if (!categoryRepository.existsByName(categoryName)) {
                categoryRepository.save(new Category(categoryName));
            }
        }
    }
}
