package com.jammy.global.common.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode implements BaseCode {

    // 공통 에러
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 500, "서버 내부 오류가 발생했습니다."),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, 400, "잘못된 요청입니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, 401, "인증이 필요합니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, 403, "접근 권한이 없습니다."),
    NOT_FOUND(HttpStatus.NOT_FOUND, 404, "요청한 리소스를 찾을 수 없습니다."),
    CONFLICT(HttpStatus.CONFLICT, 409, "이미 존재하는 리소스입니다."),

    // auth
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, 409, "이미 사용 중인 이메일입니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, 404, "존재하지 않는 사용자입니다."),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, 400, "비밀번호가 일치하지 않습니다."),

    // room
    ROOM_NOT_FOUND(HttpStatus.NOT_FOUND, 404, "존재하지 않는 방입니다."),
    INVALID_INVITE_CODE(HttpStatus.NOT_FOUND, 404, "유효하지 않은 초대 코드입니다."),
    ALREADY_JOINED_ROOM(HttpStatus.CONFLICT, 409, "이미 참여 중인 방입니다."),
    ROOM_MEMBER_LIMIT_EXCEEDED(HttpStatus.CONFLICT, 409, "방 참여 인원이 가득 찼습니다."),

    // diary
    DIARY_NOT_FOUND(HttpStatus.NOT_FOUND, 404, "존재하지 않는 일기입니다."),
    INVALID_DIARY_TYPE(HttpStatus.BAD_REQUEST, 400, "유효하지 않은 일기 타입입니다."),

    // time capsule
    TIME_CAPSULE_LOCKED(HttpStatus.FORBIDDEN, 403, "아직 열 수 없는 타임캡슐입니다."),
    ;

    private final HttpStatus httpStatus;
    private final int status;
    private final String message;
}
