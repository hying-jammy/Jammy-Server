package com.jammy.diary.dto;

import com.jammy.room.domain.TimeCapsuleStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "타임캡슐 상세 조회 응답")
public record TimeCapsuleResponse(
        @Schema(description = "방 ID", example = "1")
        Long roomId,

        @Schema(description = "타임캡슐 이름", example = "부산 여행 마지막 밤")
        String timeCapsuleName,

        @Schema(description = "타임캡슐 상태", example = "LOCKED")
        TimeCapsuleStatus timeCapsuleStatus,

        @Schema(description = "타임캡슐 공개 일시", example = "2026-10-14T20:00:00")
        LocalDateTime timeCapsuleOpenAt,

        @Schema(description = "공개까지 남은 일", example = "2")
        Integer remainingDays,

        @Schema(description = "공개까지 남은 시간", example = "4")
        Integer remainingHours,

        @Schema(description = "공개까지 남은 분", example = "12")
        Integer remainingMinutes,

        @Schema(description = "참여자별 타임캡슐 기록 작성 여부")
        List<MemberStatus> members
) {
    public record MemberStatus(
            @Schema(description = "작성자 닉네임", example = "서준")
            String nickname,

            @Schema(description = "타임캡슐 기록 작성 여부", example = "true")
            boolean hasWritten
    ) {
    }
}