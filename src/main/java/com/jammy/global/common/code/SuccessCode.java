package com.jammy.global.common.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum SuccessCode implements BaseCode {

    // health_check
    HEALTH_CHECK_SUCCESS(HttpStatus.OK, 200, "헬스 체크에 성공했습니다."),

    // auth
    SIGNUP_SUCCESS(HttpStatus.CREATED, 201, "회원가입에 성공했습니다."),
    LOGIN_SUCCESS(HttpStatus.OK, 200, "로그인에 성공했습니다."),

    // room
    ROOM_CREATE_SUCCESS(HttpStatus.CREATED, 201, "방 생성에 성공했습니다."),
    ROOMS_RETRIEVED_SUCCESS(HttpStatus.OK, 200, "방 목록 조회에 성공했습니다."),
    ROOM_DETAIL_RETRIEVED_SUCCESS(HttpStatus.OK, 200, "방 상세 조회에 성공했습니다."),
    INVITE_CODE_VERIFY_SUCCESS(HttpStatus.OK, 200, "초대 코드 확인에 성공했습니다."),
    ROOM_JOIN_SUCCESS(HttpStatus.OK, 200, "방 입장에 성공했습니다."),

    // diary
    DIARY_CREATE_SUCCESS(HttpStatus.CREATED, 201, "일기 작성에 성공했습니다."),
    DIARIES_RETRIEVED_SUCCESS(HttpStatus.OK, 200, "일기 목록 조회에 성공했습니다."),

    // time capsule
    TIME_CAPSULE_RETRIEVED_SUCCESS(HttpStatus.OK, 200, "타임캡슐 조회에 성공했습니다."),
    TIME_CAPSULE_DIARIES_RETRIEVED_SUCCESS(HttpStatus.OK, 200, "타임캡슐 일기 목록 조회에 성공했습니다."),

    // file
    FILE_UPLOAD_SUCCESS(HttpStatus.OK, 200, "파일 업로드에 성공했습니다."),
    ;

    private final HttpStatus httpStatus;
    private final int status;
    private final String message;
}
