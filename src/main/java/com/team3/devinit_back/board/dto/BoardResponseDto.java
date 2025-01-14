package com.team3.devinit_back.board.dto;

import com.team3.devinit_back.board.entity.Board;
import com.team3.devinit_back.board.entity.Category;
import com.team3.devinit_back.member.entity.Member;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class BoardResponseDto {
    private String id;
    private String title;
    private String content;

    private int upCnt;
    private int commentCnt;
    private int viewCnt;

    private String memberId;
    private String nickName;
    private String profile_image;
    private Long categoryId;
    public BoardResponseDto(Board board){
        this.id = board.getId();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.upCnt = board.getUpCnt();
        Member member = board.getMember();
        if(member!= null){
            this.memberId = member.getSocialId();
            this.nickName = member.getNickName();
            this.profile_image = member.getProfileImage();
        }
        Category category = board.getCategory();
        this.categoryId = category.getId();

    }
    public static BoardResponseDto fromEntity(Board board){ return new BoardResponseDto(board);}


    //+ 댓글

}
