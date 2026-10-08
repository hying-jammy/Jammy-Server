package com.jammy.room.controller;

import com.jammy.global.common.CommonResponse;
import com.jammy.global.common.code.SuccessCode;
import com.jammy.room.dto.CreateRoomRequest;
import com.jammy.room.dto.CreateRoomResponse;
import com.jammy.room.dto.JoinRoomRequest;
import com.jammy.room.dto.JoinRoomResponse;
import com.jammy.room.dto.VerifyInviteCodeResponse;
import com.jammy.room.service.RoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/rooms")
@Tag(name = "[방]", description = "여행 방 생성, 초대 코드 확인 및 입장 API")
public class RoomController {

    private final RoomService roomService;

    @Operation(
            summary = "방 생성",
            description = "여행 정보를 입력해 방을 생성합니다. 생성자는 자동으로 참여하며, 성공 시 방 정보와 초대 코드를 반환합니다."
    )
    @PostMapping
    public ResponseEntity<CommonResponse<CreateRoomResponse>> createRoom(@Valid @RequestBody CreateRoomRequest request) {
        CreateRoomResponse response = roomService.createRoom(request);
        return ResponseEntity
                .status(SuccessCode.ROOM_CREATE_SUCCESS.getHttpStatus())
                .body(CommonResponse.success(SuccessCode.ROOM_CREATE_SUCCESS, response));
    }

    @Operation(
            summary = "초대 코드 확인",
            description = "초대 코드로 방 정보를 조회합니다. 정원이 찬 방도 조회할 수 있으며, 실제 입장은 처리하지 않습니다."
    )
    @GetMapping("/invite-code/{inviteCode}")
    public ResponseEntity<CommonResponse<VerifyInviteCodeResponse>> verifyInviteCode(@PathVariable String inviteCode) {
        VerifyInviteCodeResponse response = roomService.verifyInviteCode(inviteCode);
        return ResponseEntity
                .status(SuccessCode.INVITE_CODE_VERIFY_SUCCESS.getHttpStatus())
                .body(CommonResponse.success(SuccessCode.INVITE_CODE_VERIFY_SUCCESS, response));
    }

    @Operation(
            summary = "방 입장",
            description = "초대 코드로 방에 참여합니다. 하이픈을 생략하거나 소문자로 입력해도 됩니다."
    )
    @PostMapping("/join")
    public ResponseEntity<CommonResponse<JoinRoomResponse>> joinRoom(@Valid @RequestBody JoinRoomRequest request) {
        JoinRoomResponse response = roomService.joinRoom(request);
        return ResponseEntity
                .status(SuccessCode.ROOM_JOIN_SUCCESS.getHttpStatus())
                .body(CommonResponse.success(SuccessCode.ROOM_JOIN_SUCCESS, response));
    }
}
