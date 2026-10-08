package com.jammy.room.dto;

import com.jammy.room.domain.Room;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "방 입장 응답")
public record JoinRoomResponse(
        @Schema(description = "입장한 방 ID", example = "1")
        Long roomId
) {
    public static JoinRoomResponse from(Room room) {
        return new JoinRoomResponse(room.getId());
    }
}
