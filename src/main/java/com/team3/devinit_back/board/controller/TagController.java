package com.team3.devinit_back.board.controller;

import com.team3.devinit_back.board.entity.Tag;
import com.team3.devinit_back.board.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/tag")
@RequiredArgsConstructor
public class TagController {
    private final TagService tagService;

    @GetMapping
    public ResponseEntity<List<Tag>> getAllTags(){
        List<Tag> tags = tagService.getAllTag();
        return  ResponseEntity.ok(tags);
    }
}
