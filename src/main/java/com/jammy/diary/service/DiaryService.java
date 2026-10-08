package com.jammy.diary.service;

import com.jammy.diary.domain.Diary;
import com.jammy.diary.domain.DiaryType;
import com.jammy.diary.dto.DiaryCreateResponse;
import com.jammy.diary.repository.DiaryRepository;
import com.jammy.diary.validator.DiaryValidator;
import com.jammy.global.common.code.ErrorCode;
import com.jammy.global.exception.BusinessException;
import com.jammy.global.service.S3Service;
import com.jammy.room.domain.Room;
import com.jammy.room.repository.RoomRepository;
import com.jammy.user.domain.User;
import com.jammy.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DiaryService {

    private final DiaryRepository diaryRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final DiaryValidator diaryValidator;
    private final S3Service s3Service;

    @Transactional
    public DiaryCreateResponse createDiary(Long roomId, Long userId, String content,
                                           DiaryType type, MultipartFile image) {
        Room room = getRoom(roomId);
        diaryValidator.validateMember(roomId, userId);

        // 타임캡슐이 이미 열린 뒤에는 타임캡슐 기록을 새로 작성할 수 없음
        if (type == DiaryType.TIME_CAPSULE && diaryValidator.isCapsuleOpen(room)) {
            throw new BusinessException(ErrorCode.TIME_CAPSULE_ALREADY_OPENED);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        String imageUrl = (image == null || image.isEmpty()) ? null : s3Service.uploadImage(image);

        Diary saved = diaryRepository.save(Diary.builder()
                .room(room)
                .user(user)
                .content(content)
                .imageUrl(imageUrl)
                .type(type)
                .build());

        return new DiaryCreateResponse(saved.getId());
    }

    private Room getRoom(Long roomId) {
        return roomRepository.findById(roomId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ROOM_NOT_FOUND));
    }
}
