package com.jammy.room.service;

import com.jammy.global.common.code.ErrorCode;
import com.jammy.global.exception.BusinessException;
import com.jammy.room.domain.Room;
import com.jammy.room.dto.CreateRoomRequest;
import com.jammy.room.dto.CreateRoomResponse;
import com.jammy.room.repository.RoomRepository;
import com.jammy.roommember.domain.RoomMember;
import com.jammy.roommember.repository.RoomMemberRepository;
import com.jammy.user.domain.User;
import com.jammy.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;

@Service
@RequiredArgsConstructor
public class RoomCreator {

    private static final int MYSQL_DUPLICATE_KEY_ERROR_CODE = 1062;

    private final RoomRepository roomRepository;
    private final RoomMemberRepository roomMemberRepository;
    private final UserRepository userRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public CreateRoomResponse createRoom(CreateRoomRequest request, String inviteCode) {
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        Room room = Room.builder()
                .title(request.title())
                .inviteCode(inviteCode)
                .startDate(request.startDate())
                .endDate(request.endDate())
                .memberLimit(request.memberLimit())
                .timeCapsuleOpenAt(request.timeCapsuleOpenAt())
                .build();

        Room savedRoom;
        try {
            savedRoom = roomRepository.saveAndFlush(room);
        } catch (DataIntegrityViolationException e) {
            // 중복 오류가 나면 이번 생성 시도를 롤백
            // RoomService에서 초대 코드가 겹친 건지 확인한 뒤 재시도
            if (isDuplicateKey(e)) {
                throw new RoomDuplicateKeyException(e);
            }
            throw e;
        }

        roomMemberRepository.saveAndFlush(RoomMember.of(savedRoom, user));

        return CreateRoomResponse.from(savedRoom);
    }

    private boolean isDuplicateKey(DataIntegrityViolationException exception) {
        Throwable cause = exception.getMostSpecificCause();
        return cause instanceof SQLException sqlException
                && sqlException.getErrorCode() == MYSQL_DUPLICATE_KEY_ERROR_CODE;
    }

    static class RoomDuplicateKeyException extends RuntimeException {
        RoomDuplicateKeyException(DataIntegrityViolationException cause) {
            super(cause);
        }

        DataIntegrityViolationException getOriginalException() {
            return (DataIntegrityViolationException) getCause();
        }
    }
}
