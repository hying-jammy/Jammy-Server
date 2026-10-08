package com.jammy.diary.controller;

import com.jammy.diary.domain.DiaryType;
import com.jammy.diary.dto.*;
import com.jammy.diary.service.DiaryService;
import com.jammy.global.common.CommonResponse;
import com.jammy.global.common.code.SuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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
            @RequestParam @NotBlank @jakarta.validation.constraints.Size(max = 500) String content,
            @RequestParam DiaryType type,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) {
        DiaryCreateResponse response = diaryService.createDiary(roomId, userId, content, type, image);
        return ResponseEntity
                .status(SuccessCode.DIARY_CREATE_SUCCESS.getHttpStatus())
                .body(CommonResponse.success(SuccessCode.DIARY_CREATE_SUCCESS, response));
    }


    @GetMapping("/diaries")
    @Operation(
            summary = "방 전체 일기 조회",
            description = "피드 일기를 최신순으로 조회합니다."
    )
    public ResponseEntity<CommonResponse<List<DiaryResponse>>> getDiaries(
            @PathVariable Long roomId
    ) {
        List<DiaryResponse> response = diaryService.getDiaries(roomId);

        return ResponseEntity
                .status(SuccessCode.DIARIES_RETRIEVED_SUCCESS.getHttpStatus())
                .body(CommonResponse.success(
                        SuccessCode.DIARIES_RETRIEVED_SUCCESS,
                        response
                ));
    }

    @GetMapping("/time-capsule")
    @Operation(
            summary = "타임캡슐 조회",
            description = "공개 시각, 남은 시간, 멤버별 기록 작성 여부를 반환합니다. 기록 내용은 포함되지 않습니다."
    )
    public ResponseEntity<CommonResponse<TimeCapsuleResponse>> getTimeCapsule(
            @PathVariable Long roomId
    ) {
        TimeCapsuleResponse response = diaryService.getTimeCapsule(roomId);
        return ResponseEntity
                .status(SuccessCode.TIME_CAPSULE_RETRIEVED_SUCCESS.getHttpStatus())
                .body(CommonResponse.success(SuccessCode.TIME_CAPSULE_RETRIEVED_SUCCESS, response));
    }

    @GetMapping("/time-capsule/diaries")
    @Operation(
            summary = "타임캡슐 일기 목록 조회",
            description = "타임캡슐 공개 시각 이후에만 조회할 수 있습니다. 이전에는 403을 반환합니다."
    )
    public ResponseEntity<CommonResponse<TimeCapsuleDiariesResponse>> getTimeCapsuleDiaries(
            @PathVariable Long roomId
    ) {
        TimeCapsuleDiariesResponse response = diaryService.getTimeCapsuleDiaries(roomId);
        return ResponseEntity
                .status(SuccessCode.TIME_CAPSULE_DIARIES_RETRIEVED_SUCCESS.getHttpStatus())
                .body(CommonResponse.success(SuccessCode.TIME_CAPSULE_DIARIES_RETRIEVED_SUCCESS, response));
    }
}