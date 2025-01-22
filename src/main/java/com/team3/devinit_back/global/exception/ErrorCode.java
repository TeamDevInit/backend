package com.team3.devinit_back.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "알 수 없는 서버 에러가 발생했습니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 Refresh Token 입니다."),
    EXPIRED_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "만료된 Refresh Token 입니다."),
    EMPTY_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "Refresh Token이 없습니다."),
    ENTITY_NOT_FOUND(HttpStatus.NOT_FOUND, "요청하신 리소스를 찾을 수 없습니다."),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "유효하지 않은 입력 값입니다."),
    DUPLICATE_RESOURCE(HttpStatus.CONFLICT, "리소스가 이미 존재합니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "인증이 필요합니다."),
    DUPLICATED_NAME(HttpStatus.CONFLICT, "이미 사용 중인 닉네임입니다."),
    INVALID_USER(HttpStatus.NOT_FOUND, "해당 ID에 해당하는 회원을 찾을 수 없습니다."),
    TAG_NOT_FOUND(HttpStatus.NOT_FOUND, "태그를 찾을 수 없습니다."),
    CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "카테고리를 찾을 수 없습니다."),
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "댓글을 찾을 수 없습니다."),
    BOARD_NOT_FOUND(HttpStatus.NOT_FOUND, "게시물을 찾을 수 없습니다."),
    PROFILE_NOT_FOUND(HttpStatus.NOT_FOUND, "프로필을 찾을 수 없습니다."),
    INVALID_PROFILE_IMAGE(HttpStatus.BAD_REQUEST, "유효하지 않은 프로필 이미지입니다."),
    FOLLOW_FAILED(HttpStatus.BAD_REQUEST, "팔로우 요청에 실패했습니다."),
    UNFOLLOW_FAILED(HttpStatus.BAD_REQUEST, "언팔로우 요청에 실패했습니다."),
    SELF_FOLLOW_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "자기 자신을 팔로우할 수 없습니다."),
    RESUME_NOT_FOUND(HttpStatus.NOT_FOUND, "이력서를 찾을 수 없습니다."),
    SKILL_NOT_FOUND(HttpStatus.NOT_FOUND, "기술 스택을 찾을 수 없습니다."),
    INVALID_TAG_ID(HttpStatus.BAD_REQUEST, "유효하지 않은 스킬 태그입니다.");
    UNFOLLOW_FAILED(HttpStatus.BAD_REQUEST, "언팔로우 요청에 실패했습니다."),
    PROJECT_NOT_FOUND(HttpStatus.NOT_FOUND, "프로젝트를 찾을 수 없습니다.");

    private final HttpStatus status;
    private final String message;
}