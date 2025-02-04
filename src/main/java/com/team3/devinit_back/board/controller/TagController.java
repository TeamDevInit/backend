package com.team3.devinit_back.board.controller;

import com.team3.devinit_back.board.entity.Tag;
import com.team3.devinit_back.board.service.TagService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/tags")
@RequiredArgsConstructor
public class TagController {
    private final TagService tagService;

    @GetMapping
    @Operation(
            summary = "카테고리 조회",
            description = "DB에 저장된 태그 종류를 반환합니다.")
    public ResponseEntity<List<Tag>> getAllTags(){
        List<Tag> tags = tagService.getAllTag();
        return  ResponseEntity.ok(tags);
    }
}
