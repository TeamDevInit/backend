package com.team3.devinit_back.board.dto;

import com.team3.devinit_back.board.entity.Board;
import com.team3.devinit_back.board.entity.Category;
import com.team3.devinit_back.comment.dto.CommentResponseDto;
import com.team3.devinit_back.member.entity.Member;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Getter
public class BoardDetailResponseDto {
    private Long id;
    private String title;
    private String content;

    private int upCnt;
    private int commentCnt;
    private int viewCnt;

    private String memberId;
    private String nickName;
    private String profileImage;
    private Long categoryId;
    private String categoryName;

    private List<CommentResponseDto> comment;
    private List<TagResponseDto> tag;

    private boolean isRecommended;
    private boolean isFollowing;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    public BoardDetailResponseDto(Board board){
        this.id = board.getId();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.upCnt = board.getUpCnt();
        this.viewCnt = board.getViewCnt();

        Member member = board.getMember();
        if(member!= null){
            this.memberId = member.getId();
            this.nickName = member.getNickName();
            this.profileImage = member.getProfileImage();
        }

        Category category = board.getCategory();
        this.categoryId = category.getId();
        this.categoryName = category.getName();

        this.comment = board.getComment() != null ? board.getComment().stream()
                .map(CommentResponseDto::fromEntity)
                .toList() : new ArrayList<>();
        this.commentCnt = board.getCommentCnt();

        this.tag = board.getTagBoards() != null ? board.getTagBoards().stream()
                .map(tagBoard -> new TagResponseDto(tagBoard.getTag()))
                .toList() : new ArrayList<>();


        this.createdAt = board.getCreatedAt();
        this.updatedAt = board.getUpdatedAt();
    }
    public static BoardDetailResponseDto fromEntity(Board board){ return new BoardDetailResponseDto(board);}
    public void setRecommended(boolean isRecommended) {
        this.isRecommended = isRecommended;
    }
    public void setFollowing(boolean isFollowing){
        this.isFollowing = isFollowing;
    }
}
