package com.jammy.diary.dto;

import com.jammy.diary.domain.Diary;
import com.jammy.diary.domain.DiaryType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "일기 응답")
public record DiaryResponse(
        @Schema(description = "일기 ID", example = "1") Long diaryId,
        @Schema(description = "작성자 닉네임", example = "민지") String nickname,
        @Schema(description = "일기 내용", example = "오늘 해운대 날씨 진짜 좋다!") String content,
        @Schema(description = "이미지 URL (없으면 null)") String imageUrl,
        @Schema(description = "일기 타입", example = "PUBLIC") DiaryType type,
        @Schema(description = "작성 시각") LocalDateTime createdAt
) {
    public static DiaryResponse from(Diary diary) {
        return new DiaryResponse(
                diary.getId(),
                diary.getUser().getNickname(),
                diary.getContent(),
                diary.getImageUrl(),
                diary.getType(),
                diary.getCreatedAt()
        );
    }
}
