package com.jammy.room.validator;

import com.jammy.global.common.code.ErrorCode;
import com.jammy.global.exception.BusinessException;
import com.jammy.room.domain.Room;
import com.jammy.room.dto.CreateRoomRequest;
import com.jammy.roommember.repository.RoomMemberRepository;
import com.jammy.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RoomValidator {

    private final RoomMemberRepository roomMemberRepository;

    public void validateCreateRoom(CreateRoomRequest request) {
        validateTravelPeriod(request);
        validateTimeCapsuleOpenAt(request);
    }

    public void validateJoinRoom(User user, Room room) {
        if (roomMemberRepository.existsByRoomIdAndUserId(room.getId(), user.getId())) {
            throw new BusinessException(ErrorCode.ALREADY_JOINED_ROOM);
        }

        if (roomMemberRepository.countByRoomId(room.getId()) >= room.getMemberLimit()) {
            throw new BusinessException(ErrorCode.ROOM_MEMBER_LIMIT_EXCEEDED);
        }
    }

    private void validateTravelPeriod(CreateRoomRequest request) {
        if (request.startDate() != null && request.endDate() != null
                && request.endDate().isBefore(request.startDate())) {
            throw new BusinessException(ErrorCode.INVALID_TRAVEL_PERIOD);
        }
    }

    private void validateTimeCapsuleOpenAt(CreateRoomRequest request) {
        if (request.endDate() != null && request.timeCapsuleOpenAt() != null
                && request.timeCapsuleOpenAt().toLocalDate().isBefore(request.endDate())) {
            throw new BusinessException(ErrorCode.INVALID_TIME_CAPSULE_OPEN_AT);
        }
    }
}
