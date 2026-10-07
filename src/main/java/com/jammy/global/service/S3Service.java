package com.jammy.global.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.jammy.global.common.code.ErrorCode;
import com.jammy.global.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Service {

    private static final String IMAGE_DIRECTORY = "images/";
    private static final String DEFAULT_CONTENT_TYPE = "application/octet-stream";

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String uploadImage(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BusinessException(ErrorCode.EMPTY_FILE);
        }

        String fileKey = createFileKey(file.getOriginalFilename());
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(resolveContentType(file.getContentType()));

        try {
            amazonS3Client.putObject(bucket, fileKey, file.getInputStream(), metadata);
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.FILE_UPLOAD_FAILED);
        }

        return amazonS3Client.getUrl(bucket, fileKey).toString();
    }

    private String createFileKey(String originalFilename) {
        String extension = StringUtils.getFilenameExtension(originalFilename);
        String filename = UUID.randomUUID().toString();

        if (!StringUtils.hasText(extension)) {
            return IMAGE_DIRECTORY + filename;
        }

        return IMAGE_DIRECTORY + filename + "." + extension;
    }

    private String resolveContentType(String contentType) {
        if (!StringUtils.hasText(contentType)) {
            return DEFAULT_CONTENT_TYPE;
        }

        return contentType;
    }
}
