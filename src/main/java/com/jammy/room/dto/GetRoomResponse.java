package com.jammy.room.dto;

import com.jammy.room.domain.Room;
import com.jammy.room.domain.TimeCapsuleStatus;
import com.jammy.roommember.domain.RoomMember;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "방 상세 조회 응답")
public record GetRoomResponse(
        @Schema(description = "방 ID", example = "1")
        Long roomId,

        @Schema(description = "여행 제목", example = "부산 여행")
        String title,

        @Schema(description = "초대 코드", example = "A7K-4F2M")
        String inviteCode,

        @Schema(description = "여행 시작일", example = "2026-10-12")
        LocalDate startDate,

        @Schema(description = "여행 종료일", example = "2026-10-14")
        LocalDate endDate,

        @Schema(description = "참여자 목록")
        List<MemberResponse> members,

        @Schema(description = "타임캡슐 공개 일시", example = "2026-10-14T20:00:00")
        LocalDateTime timeCapsuleOpenAt,

        @Schema(description = "타임캡슐 상태", example = "LOCKED")
        TimeCapsuleStatus timeCapsuleStatus,

        @Schema(description = "타임캡슐 공개 D-day (당일 0, 공개일 이후 음수)", example = "2")
        Integer timeCapsuleDday
) {
    public static GetRoomResponse from(Room room, List<RoomMember> roomMembers, LocalDateTime now) {
        List<MemberResponse> members = roomMembers.stream()
                .map(roomMember -> new MemberResponse(roomMember.getUser().getNickname()))
                .toList();

        return new GetRoomResponse(
                room.getId(),
                room.getTitle(),
                room.getInviteCode(),
                room.getStartDate(),
                room.getEndDate(),
                members,
                room.getTimeCapsuleOpenAt(),
                room.getTimeCapsuleStatus(now),
                room.getTimeCapsuleDday(now.toLocalDate())
        );
    }

    public record MemberResponse(
            @Schema(description = "참여자 닉네임", example = "민지")
            String nickname
    ) {
    }
}
