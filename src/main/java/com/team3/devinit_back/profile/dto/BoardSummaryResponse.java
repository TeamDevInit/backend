package com.team3.devinit_back.profile.dto;

import com.team3.devinit_back.board.entity.Board;
import com.team3.devinit_back.board.entity.Category;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BoardSummaryResponse {
    private Long id;
    private String title;
    private int upCnt;
    private int commentCnt;
    private int viewCnt;
    private String categoryName;

    public static BoardSummaryResponse fromEntity(Board board) {
        Category category = board.getCategory();

        return BoardSummaryResponse.builder()
            .id(board.getId())
            .title(board.getTitle())
            .upCnt(board.getUpCnt())
            .commentCnt(board.getCommentCnt())
            .viewCnt(board.getViewCnt())
            .categoryName(category != null ? category.getName() : "카테고리 없음")
            .build();
    }
}