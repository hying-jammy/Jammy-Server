package com.jammy.room.controller;

import com.jammy.global.common.CommonResponse;
import com.jammy.global.common.code.SuccessCode;
import com.jammy.room.dto.CreateRoomRequest;
import com.jammy.room.dto.CreateRoomResponse;
import com.jammy.room.dto.GetRoomResponse;
import com.jammy.room.dto.GetRoomsResponse;
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

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Tag(name = "[방]", description = "여행 방 생성, 입장 및 조회 API")
public class RoomController {

    private final RoomService roomService;

    @Operation(
            summary = "방 생성",
            description = "여행 정보를 입력해 방을 생성합니다. 생성자는 자동으로 참여하며, 성공 시 방 정보와 초대 코드를 반환합니다."
    )
    @PostMapping("/rooms")
    public ResponseEntity<CommonResponse<CreateRoomResponse>> createRoom(@Valid @RequestBody CreateRoomRequest request) {
        CreateRoomResponse response = roomService.createRoom(request);
        return ResponseEntity
                .status(SuccessCode.ROOM_CREATE_SUCCESS.getHttpStatus())
                .body(CommonResponse.success(SuccessCode.ROOM_CREATE_SUCCESS, response));
    }

    @Operation(
            summary = "참여 중인 방 목록 조회",
            description = "사용자가 참여 중인 방을 여행 시작일 최신순으로 조회합니다. 참여 중인 방이 없으면 빈 목록을 반환합니다."
    )
    @GetMapping("/users/{userId}/rooms")
    public ResponseEntity<CommonResponse<List<GetRoomsResponse>>> getRooms(@PathVariable Long userId) {
        List<GetRoomsResponse> response = roomService.getRooms(userId);
        return ResponseEntity
                .status(SuccessCode.ROOMS_RETRIEVED_SUCCESS.getHttpStatus())
                .body(CommonResponse.success(SuccessCode.ROOMS_RETRIEVED_SUCCESS, response));
    }

    @Operation(
            summary = "방 상세 조회",
            description = "방 정보, 참여자 목록 및 타임캡슐 정보를 조회합니다."
    )
    @GetMapping("/rooms/{roomId}")
    public ResponseEntity<CommonResponse<GetRoomResponse>> getRoom(@PathVariable Long roomId) {
        GetRoomResponse response = roomService.getRoom(roomId);
        return ResponseEntity
                .status(SuccessCode.ROOM_DETAIL_RETRIEVED_SUCCESS.getHttpStatus())
                .body(CommonResponse.success(SuccessCode.ROOM_DETAIL_RETRIEVED_SUCCESS, response));
    }

    @Operation(
            summary = "초대 코드 확인",
            description = "초대 코드로 방 정보를 조회합니다. 정원이 찬 방도 조회할 수 있으며, 실제 입장은 처리하지 않습니다."
    )
    @GetMapping("/rooms/invite-code/{inviteCode}")
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
    @PostMapping("/rooms/join")
    public ResponseEntity<CommonResponse<JoinRoomResponse>> joinRoom(@Valid @RequestBody JoinRoomRequest request) {
        JoinRoomResponse response = roomService.joinRoom(request);
        return ResponseEntity
                .status(SuccessCode.ROOM_JOIN_SUCCESS.getHttpStatus())
                .body(CommonResponse.success(SuccessCode.ROOM_JOIN_SUCCESS, response));
    }
}
