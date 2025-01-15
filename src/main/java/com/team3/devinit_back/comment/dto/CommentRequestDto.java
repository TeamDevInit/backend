package com.team3.devinit_back.comment.dto;

import com.team3.devinit_back.comment.entity.Comment;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.Data;
import lombok.Getter;


@Data
@Getter
public class CommentRequestDto {

    @Column(nullable = false)
    private String content;
    private Long boardId;
    private Long parentCommentId;


}
