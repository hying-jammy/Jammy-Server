package com.jammy.room.dto;

import com.jammy.room.domain.Room;
import com.jammy.roommember.domain.RoomMember;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.List;

@Schema(description = "초대 코드 확인 응답")
public record VerifyInviteCodeResponse(
        @Schema(description = "방 식별자", example = "1")
        Long roomId,

        @Schema(description = "여행 제목", example = "부산 여행")
        String title,

        @Schema(description = "여행 시작일", example = "2026-10-12")
        LocalDate startDate,

        @Schema(description = "여행 종료일", example = "2026-10-14")
        LocalDate endDate,

        @Schema(description = "현재 참여 인원", example = "2")
        Integer memberCount,

        @Schema(description = "최대 참여 인원", example = "3")
        Integer memberLimit,

        @Schema(description = "현재 참여자 목록")
        List<MemberResponse> members
) {
    public static VerifyInviteCodeResponse from(Room room, List<RoomMember> roomMembers) {
        List<MemberResponse> members = roomMembers.stream()
                .map(roomMember -> new MemberResponse(roomMember.getUser().getNickname()))
                .toList();

        return new VerifyInviteCodeResponse(
                room.getId(), room.getTitle(), room.getStartDate(), room.getEndDate(),
                members.size(), room.getMemberLimit(), members
        );
    }

    public record MemberResponse(
            @Schema(description = "참여자 닉네임", example = "민지")
            String nickname
    ) {
    }
}
