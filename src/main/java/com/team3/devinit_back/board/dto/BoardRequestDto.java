package com.team3.devinit_back.board.dto;

import lombok.Data;

@Data
public class BoardRequestDto {
    private String title;
    private String content;
    private Long categoryId;

}
