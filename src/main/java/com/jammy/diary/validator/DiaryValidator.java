package com.jammy.diary.validator;

import com.jammy.global.common.code.ErrorCode;
import com.jammy.global.exception.BusinessException;
import com.jammy.room.domain.Room;
import com.jammy.roommember.repository.RoomMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class DiaryValidator {

    private final RoomMemberRepository roomMemberRepository;
    private final Clock clock;

    public void validateMember(Long roomId, Long userId) {
        if (!roomMemberRepository.existsByRoomIdAndUserId(roomId, userId)) {
            throw new BusinessException(ErrorCode.FORBIDDEN);
        }
    }

    public boolean isCapsuleOpen(Room room) {
        return !LocalDateTime.now(clock).isBefore(room.getTimeCapsuleOpenAt());
    }
}
