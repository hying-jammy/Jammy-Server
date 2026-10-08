package com.jammy.room.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Schema(description = "방 입장 요청")
public record JoinRoomRequest(
        @Schema(description = "입장하는 사용자 ID", example = "1")
        @NotNull
        @Positive
        Long userId,

        @Schema(description = "친구에게 받은 영문·숫자 7자리 초대 코드", example = "A7K-4F2M")
        @NotBlank
        String inviteCode
) {
}
