package com.jammy.diary.controller;

import com.jammy.diary.domain.DiaryType;
import com.jammy.diary.dto.DiaryCreateResponse;
import com.jammy.diary.service.DiaryService;
import com.jammy.global.common.CommonResponse;
import com.jammy.global.common.code.SuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/rooms/{roomId}")
@Tag(name = "[일기/타임캡슐]", description = "일기 작성, 목록 조회, 타임캡슐 조회 API")
public class DiaryController {

    private final DiaryService diaryService;

    @Operation(
            summary = "일기 작성",
            description = "공개 일기(PUBLIC) 또는 타임캡슐 기록(TIME_CAPSULE)을 작성합니다. 사진은 선택이며 글은 500자 이내입니다."
    )
    @PostMapping(value = "/diaries", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CommonResponse<DiaryCreateResponse>> createDiary(
            @PathVariable Long roomId,
            @RequestParam @NotNull @Positive Long userId,
            @RequestParam @NotBlank @Size(max = 500) String content,
            @RequestParam DiaryType type,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) {
        DiaryCreateResponse response = diaryService.createDiary(roomId, userId, content, type, image);
        return ResponseEntity
                .status(SuccessCode.DIARY_CREATE_SUCCESS.getHttpStatus())
                .body(CommonResponse.success(SuccessCode.DIARY_CREATE_SUCCESS, response));
    }
}
