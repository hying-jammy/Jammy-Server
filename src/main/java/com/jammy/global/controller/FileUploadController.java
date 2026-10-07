package com.jammy.global.controller;

import com.jammy.global.common.CommonResponse;
import com.jammy.global.common.code.SuccessCode;
import com.jammy.global.service.S3Service;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/files")
@Tag(name = "[파일]", description = "S3 업로드 동작 확인용 테스트 API")
public class FileUploadController {

    private final S3Service s3Service;

    @Operation(
            summary = "이미지 업로드 테스트",
            description = """
                    S3 업로드 설정이 정상 동작하는지 확인하기 위한 테스트용 API입니다.
                    
                    실제 일기 작성 기능에서는 별도 업로드 API를 호출하지 않고,
                    일기 작성 요청 안에서 이미지 파일을 함께 받아 S3에 저장하는 방식으로 사용할 예정입니다.
                    """
    )
    @PostMapping(value = "/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CommonResponse<String>> uploadImage(@RequestParam("file") MultipartFile file) {
        String imageUrl = s3Service.uploadImage(file);
        return ResponseEntity
                .status(SuccessCode.FILE_UPLOAD_SUCCESS.getHttpStatus())
                .body(CommonResponse.success(SuccessCode.FILE_UPLOAD_SUCCESS, imageUrl));
    }
}
