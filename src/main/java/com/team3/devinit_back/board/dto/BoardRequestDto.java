package com.team3.devinit_back.board.dto;

import lombok.Data;

import java.util.List;

@Data
public class BoardRequestDto {
    private String title;
    private String content;
    private Long categoryId;
    private List<String> tags;

}
