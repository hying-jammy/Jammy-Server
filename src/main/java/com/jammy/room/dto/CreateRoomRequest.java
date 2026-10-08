package com.jammy.room.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Schema(description = "방 생성 요청")
public record CreateRoomRequest(
        @Schema(description = "방을 생성하는 사용자 ID", example = "1")
        @NotNull
        @Positive
        Long userId,

        @Schema(description = "여행 제목", example = "부산 여행")
        @NotBlank
        @Size(max = 50)
        String title,

        @Schema(description = "여행 시작일", example = "2026-10-12")
        @NotNull
        LocalDate startDate,

        @Schema(description = "여행 시작일 당일 또는 이후의 종료일", example = "2026-10-14")
        @NotNull
        LocalDate endDate,

        @Schema(description = "생성자를 포함한 최대 참여 인원 (1명 이상)", example = "3")
        @NotNull
        @Min(1)
        Integer memberLimit,

        @Schema(description = "여행 종료일 당일 또는 이후의 타임캡슐 공개 일시", example = "2026-10-14T20:00:00")
        @NotNull
        LocalDateTime timeCapsuleOpenAt
) {
}
