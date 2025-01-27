package com.team3.devinit_back.board.controller;

import com.team3.devinit_back.board.entity.Category;
import com.team3.devinit_back.board.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<Category>> getAllCategories() {
        List<Category> categories = categoryService.getAllCategory();
        System.out.println(categories);
        return ResponseEntity.ok(categories);
    }
}
