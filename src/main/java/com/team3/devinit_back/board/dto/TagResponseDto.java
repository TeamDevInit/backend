package com.team3.devinit_back.board.dto;

import com.team3.devinit_back.board.entity.Tag;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class TagResponseDto {
    private Long tagId;    // 태그 ID
    private String tagName; // 태그 이름

    public TagResponseDto(Tag tag) {
        this.tagId = tag.getId();
        this.tagName = tag.getName();
    }
}