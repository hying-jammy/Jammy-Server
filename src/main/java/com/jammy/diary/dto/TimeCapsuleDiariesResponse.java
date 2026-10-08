package com.jammy.diary.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "타임캡슐 일기 목록 응답")
public record TimeCapsuleDiariesResponse(
        @Schema(description = "방 참여 인원", example = "3") int memberCount,
        @Schema(description = "타임캡슐 기록 수", example = "3") int diaryCount,
        @Schema(description = "타임캡슐 기록 목록") List<DiaryResponse> diaries
) {
}
