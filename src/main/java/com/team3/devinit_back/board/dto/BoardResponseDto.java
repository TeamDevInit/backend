package com.team3.devinit_back.board.dto;

import com.team3.devinit_back.board.entity.Board;
import com.team3.devinit_back.board.entity.Category;
import com.team3.devinit_back.member.entity.Member;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;

@Data
@Getter
public class BoardResponseDto {
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

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    public BoardResponseDto(Board board){
        this.id = board.getId();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.upCnt = board.getUpCnt();
        Member member = board.getMember();
        if(member!= null){
            this.memberId = member.getId();
            this.nickName = member.getNickName();
            this.profileImage = member.getProfileImage();
        }
        Category category = board.getCategory();
        this.categoryId = category.getId();

        this.createdAt = board.getCreatedAt();
        this.updatedAt = board.getUpdatedAt();

    }
    public static BoardResponseDto fromEntity(Board board){ return new BoardResponseDto(board);}


    //+ 댓글

}
