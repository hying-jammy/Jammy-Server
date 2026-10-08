package com.jammy.diary.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "일기 작성 응답")
public record DiaryCreateResponse(@Schema(description = "생성된 일기 ID", example = "1") Long diaryId
) {

}
