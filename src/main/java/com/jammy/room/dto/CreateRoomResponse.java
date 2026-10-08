package com.jammy.room.dto;

import com.jammy.room.domain.Room;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Schema(description = "방 생성 응답")
public record CreateRoomResponse(
        @Schema(description = "방 ID", example = "1")
        Long roomId,

        @Schema(description = "친구들에게 공유할 초대 코드", example = "A7K-4F2M")
        String inviteCode,

        @Schema(description = "여행 제목", example = "부산 여행")
        String title,

        @Schema(description = "여행 시작일", example = "2026-10-12")
        LocalDate startDate,

        @Schema(description = "여행 종료일", example = "2026-10-14")
        LocalDate endDate,

        @Schema(description = "최대 참여 인원", example = "3")
        Integer memberLimit,

        @Schema(description = "타임캡슐 공개 일시", example = "2026-10-14T20:00:00")
        LocalDateTime timeCapsuleOpenAt
) {
    public static CreateRoomResponse from(Room room) {
        return new CreateRoomResponse(
                room.getId(),
                room.getInviteCode(),
                room.getTitle(),
                room.getStartDate(),
                room.getEndDate(),
                room.getMemberLimit(),
                room.getTimeCapsuleOpenAt()
        );
    }
}
