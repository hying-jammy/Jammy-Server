package com.jammy.diary.service;

import com.jammy.diary.domain.Diary;
import com.jammy.diary.domain.DiaryType;
import com.jammy.diary.dto.*;
import com.jammy.diary.repository.DiaryRepository;
import com.jammy.diary.validator.DiaryValidator;
import com.jammy.global.common.code.ErrorCode;
import com.jammy.global.exception.BusinessException;
import com.jammy.global.service.S3Service;
import com.jammy.room.domain.Room;
import com.jammy.room.domain.TimeCapsuleStatus;
import com.jammy.room.repository.RoomRepository;
import com.jammy.roommember.repository.RoomMemberRepository;
import com.jammy.user.domain.User;
import com.jammy.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.Clock;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DiaryService {

    private final DiaryRepository diaryRepository;
    private final RoomRepository roomRepository;
    private final RoomMemberRepository roomMemberRepository;
    private final UserRepository userRepository;
    private final DiaryValidator diaryValidator;
    private final S3Service s3Service;
    private final Clock clock;

    @Transactional
    public DiaryCreateResponse createDiary(
            Long roomId,
            Long userId,
            String content,
            DiaryType type,
            MultipartFile image
    ) {
        Room room = getRoom(roomId);
        diaryValidator.validateMember(roomId, userId);

        // 타임캡슐이 이미 열린 뒤에는 타임캡슐 기록을 새로 작성할 수 없음
        if (type == DiaryType.TIME_CAPSULE && diaryValidator.isCapsuleOpen(room)) {
            throw new BusinessException(ErrorCode.TIME_CAPSULE_ALREADY_OPENED);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        String imageUrl = (image == null || image.isEmpty())
                ? null
                : s3Service.uploadImage(image);

        Diary saved = diaryRepository.save(
                Diary.builder()
                        .room(room)
                        .user(user)
                        .content(content)
                        .imageUrl(imageUrl)
                        .type(type)
                        .build()
        );

        return new DiaryCreateResponse(saved.getId());
    }


    public List<DiaryResponse> getDiaries(Long roomId) {
        getRoom(roomId);

        return diaryRepository
                .findByRoomIdOrderByIdDesc(roomId)
                .stream()
                .map(DiaryResponse::from)
                .toList();
    }

    // 타임캡슐 정보 (내용 없이 상태만)
    public TimeCapsuleResponse getTimeCapsule(Long roomId) {
        Room room = getRoom(roomId);

        LocalDateTime now = LocalDateTime.now(clock);
        LocalDateTime openAt = room.getTimeCapsuleOpenAt();

        boolean isOpen = !now.isBefore(openAt);

        TimeCapsuleStatus status = isOpen
                ? TimeCapsuleStatus.OPENED
                : TimeCapsuleStatus.LOCKED;

        long remainingSeconds = isOpen
                ? 0
                : Duration.between(now, openAt).getSeconds();

        int remainingDays =
                (int) (remainingSeconds / (24 * 60 * 60));

        int remainingHours =
                (int) ((remainingSeconds % (24 * 60 * 60))
                        / (60 * 60));

        int remainingMinutes =
                (int) ((remainingSeconds % (60 * 60))
                        / 60);

        // 타임캡슐을 작성한 사용자 ID
        Set<Long> writerIds =
                diaryRepository.findCapsuleWriterIds(roomId);

        // 방 참여자별 타임캡슐 작성 여부
        List<TimeCapsuleResponse.MemberStatus> members =
                roomMemberRepository
                        .findByRoomIdOrderByIdAsc(roomId)
                        .stream()
                        .map(member ->
                                new TimeCapsuleResponse.MemberStatus(
                                        member.getUser().getNickname(),
                                        writerIds.contains(
                                                member.getUser().getId()
                                        )
                                )
                        )
                        .toList();

        return new TimeCapsuleResponse(
                roomId,
                room.getTitle() + "마지막 밤",
                status,
                openAt,
                remainingDays,
                remainingHours,
                remainingMinutes,
                members
        );
    }

    // 타임캡슐 공개 후 기록 목록
    public TimeCapsuleDiariesResponse getTimeCapsuleDiaries(
            Long roomId
    ) {
        Room room = getRoom(roomId);

        if (!diaryValidator.isCapsuleOpen(room)) {
            throw new BusinessException(
                    ErrorCode.TIME_CAPSULE_LOCKED
            );
        }

        List<DiaryResponse> diaries =
                diaryRepository
                        .findByRoomIdAndTypeOrderByIdAsc(
                                roomId,
                                DiaryType.TIME_CAPSULE
                        )
                        .stream()
                        .map(DiaryResponse::from)
                        .toList();

        int memberCount =
                (int) roomMemberRepository.countByRoomId(roomId);

        return new TimeCapsuleDiariesResponse(
                memberCount,
                diaries.size(),
                diaries
        );
    }

    private Room getRoom(Long roomId) {
        return roomRepository.findById(roomId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ROOM_NOT_FOUND));
    }
}