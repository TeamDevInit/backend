package com.team3.devinit_back.comment.dto;

import com.team3.devinit_back.comment.entity.Comment;
import com.team3.devinit_back.member.entity.Member;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;

@Data
@Getter
public class CommentResponseDto {

    private Long id;
    private String content;
    private Long parentCommentId;

    private Long boardId;

    private String memberId;
    private String profileImage;
    private String nickName;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public CommentResponseDto(Comment comment){
        this.id = comment.getId();
        this.content= comment.getContent();
        this.parentCommentId = comment.getParentComment().getId();
        this.boardId = comment.getBoard().getId();
        Member member = comment.getMember();
        if(member != null){
            this.memberId = member.getId();
            this.nickName = member.getNickName();
            this.profileImage = member.getProfileImage();
        }
        this.createdAt = comment.getCreatedAt();
        this.updatedAt = comment.getUpdatedAt();
    }

    public  static CommentResponseDto fromEntity(Comment comment){return  new CommentResponseDto(comment);}
}
