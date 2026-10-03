package com.jammy.global.common.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum SuccessCode implements BaseCode {

    // health_check
    HEALTH_CHECK_SUCCESS(HttpStatus.OK, 200, "Health Check Success"),

    // auth
    SIGNUP_SUCCESS(HttpStatus.CREATED, 201, "Signup Success"),
    LOGIN_SUCCESS(HttpStatus.OK, 200, "Login Success"),

    // room
    ROOM_CREATE_SUCCESS(HttpStatus.CREATED, 201, "Room Create Success"),
    ROOMS_RETRIEVED_SUCCESS(HttpStatus.OK, 200, "Rooms Retrieved Success"),
    ROOM_DETAIL_RETRIEVED_SUCCESS(HttpStatus.OK, 200, "Room Detail Retrieved Success"),
    INVITE_CODE_VERIFY_SUCCESS(HttpStatus.OK, 200, "Invite Code Verify Success"),
    ROOM_JOIN_SUCCESS(HttpStatus.CREATED, 201, "Room Join Success"),

    // diary
    DIARY_CREATE_SUCCESS(HttpStatus.CREATED, 201, "Diary Create Success"),
    DIARIES_RETRIEVED_SUCCESS(HttpStatus.OK, 200, "Diaries Retrieved Success"),

    // time capsule
    TIME_CAPSULE_RETRIEVED_SUCCESS(HttpStatus.OK, 200, "Time Capsule Retrieved Success"),
    TIME_CAPSULE_DIARIES_RETRIEVED_SUCCESS(HttpStatus.OK, 200, "Time Capsule Diaries Retrieved Success"),
    ;

    private final HttpStatus httpStatus;
    private final int status;
    private final String message;
}
