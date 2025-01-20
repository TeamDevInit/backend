package com.team3.devinit_back.profile.dto;

import com.team3.devinit_back.board.dto.TagResponseDto;
import com.team3.devinit_back.board.entity.Board;
import com.team3.devinit_back.board.entity.Category;
import com.team3.devinit_back.member.entity.Member;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class BoardSummaryResponse {
    private Long id;
    private String title;
    private String content;
    private String thumbnail;
    private int upCnt;
    private int commentCnt;
    private int viewCnt;

    private String memberId;
    private String nickName;
    private String profileImage;
    private Long categoryId;
    private String categoryName;
    private List<TagResponseDto> tags;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static BoardSummaryResponse fromEntity(Board board) {
        Member member = board.getMember();
        Category category = board.getCategory();

        return BoardSummaryResponse.builder()
            .id(board.getId())
            .title(board.getTitle())
            .content(board.getContent())
            .thumbnail(board.getThumbnail())
            .upCnt(board.getUpCnt())
            .commentCnt(board.getCommentCnt())
            .viewCnt(board.getViewCnt())
            .memberId(member != null ? member.getId() : null)
            .nickName(member != null ? member.getNickName() : null)
            .profileImage(member != null ? member.getProfileImage() : null)
            .categoryId(category != null ? category.getId() : null)
            .categoryName(category != null ? category.getName() : "카테고리 없음")
            .tags(board.getTagBoards() != null ? board.getTagBoards().stream()
                .map(tagBoard -> new TagResponseDto(tagBoard.getTag()))
                .toList() : new ArrayList<>())
            .createdAt(board.getCreatedAt())
            .updatedAt(board.getUpdatedAt())
            .build();
    }
}