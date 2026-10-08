package com.jammy.room.service;

import com.jammy.global.common.code.ErrorCode;
import com.jammy.global.exception.BusinessException;
import com.jammy.room.domain.Room;
import com.jammy.room.dto.CreateRoomRequest;
import com.jammy.room.dto.CreateRoomResponse;
import com.jammy.room.dto.GetRoomResponse;
import com.jammy.room.dto.GetRoomsResponse;
import com.jammy.room.dto.JoinRoomRequest;
import com.jammy.room.dto.JoinRoomResponse;
import com.jammy.room.dto.VerifyInviteCodeResponse;
import com.jammy.room.repository.RoomRepository;
import com.jammy.room.support.RoomInviteCodeGenerator;
import com.jammy.room.validator.RoomValidator;
import com.jammy.roommember.domain.RoomMember;
import com.jammy.roommember.repository.RoomMemberRepository;
import com.jammy.user.domain.User;
import com.jammy.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoomService {

    private static final int MAX_INVITE_CODE_ATTEMPTS = 10;
    private static final ZoneId TIME_ZONE = ZoneId.of("Asia/Seoul");

    private final RoomRepository roomRepository;
    private final RoomMemberRepository roomMemberRepository;
    private final UserRepository userRepository;
    private final RoomCreator roomCreator;
    private final RoomInviteCodeGenerator roomInviteCodeGenerator;
    private final RoomValidator roomValidator;

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public CreateRoomResponse createRoom(CreateRoomRequest request) {
        roomValidator.validateCreateRoom(request);

        for (int attempt = 0; attempt < MAX_INVITE_CODE_ATTEMPTS; attempt++) {
            String inviteCode = roomInviteCodeGenerator.generateInviteCode();
            try {
                return roomCreator.createRoom(request, inviteCode);
            } catch (RoomCreator.RoomDuplicateKeyException e) {
                // 실패한 생성 작업이 롤백된 뒤, 초대 코드 중복 여부를 확인
                if (!roomRepository.existsByInviteCode(inviteCode)) {
                    throw e.getOriginalException();
                }
            }
        }

        throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
    }

    public List<GetRoomsResponse> getRooms(Long userId) {
        if (userId == null || userId < 1) {
            throw new BusinessException(ErrorCode.BAD_REQUEST);
        }
        if (!userRepository.existsById(userId)) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }

        List<Room> joinedRooms = roomMemberRepository.findByUserIdOrderByRoomStartDateDescRoomIdDesc(userId).stream()
                .map(RoomMember::getRoom)
                .toList();
        if (joinedRooms.isEmpty()) {
            return List.of();
        }

        Map<Long, Long> memberCounts = getMemberCounts(joinedRooms);
        LocalDateTime now = LocalDateTime.now(TIME_ZONE);

        return joinedRooms.stream()
                .map(room -> GetRoomsResponse.from(room, memberCounts.getOrDefault(room.getId(), 0L), now))
                .toList();
    }

    public GetRoomResponse getRoom(Long roomId) {
        if (roomId == null || roomId < 1) {
            throw new BusinessException(ErrorCode.BAD_REQUEST);
        }

        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ROOM_NOT_FOUND));
        List<RoomMember> roomMembers = roomMemberRepository.findByRoomIdOrderByIdAsc(roomId);

        return GetRoomResponse.from(room, roomMembers, LocalDateTime.now(TIME_ZONE));
    }

    public VerifyInviteCodeResponse verifyInviteCode(String inviteCode) {
        inviteCode = normalizeInviteCode(inviteCode);

        Room room = roomRepository.findRoomByInviteCode(inviteCode)
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_INVITE_CODE));
        List<RoomMember> roomMembers = roomMemberRepository.findByRoomIdOrderByIdAsc(room.getId());

        return VerifyInviteCodeResponse.from(room, roomMembers);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public JoinRoomResponse joinRoom(JoinRoomRequest request) {
        String inviteCode = normalizeInviteCode(request.inviteCode());

        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        // 동시 입장으로 정원을 초과하지 않도록 처리
        Room room = roomRepository.findByInviteCode(inviteCode)
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_INVITE_CODE));

        roomValidator.validateJoinRoom(user, room);

        roomMemberRepository.save(RoomMember.of(room, user));

        return JoinRoomResponse.from(room);
    }

    private Map<Long, Long> getMemberCounts(List<Room> joinedRooms) {
        List<Long> roomIds = joinedRooms.stream()
                .map(Room::getId)
                .toList();

        return roomMemberRepository.countMembersByRoomIdIn(roomIds).stream()
                .collect(Collectors.toMap(
                        RoomMemberRepository.MemberCount::getRoomId,
                        RoomMemberRepository.MemberCount::getMemberCount
                ));
    }

    private String normalizeInviteCode(String inviteCode) {
        if (inviteCode == null) {
            throw new BusinessException(ErrorCode.BAD_REQUEST);
        }

        inviteCode = inviteCode.strip().toUpperCase(Locale.ROOT);
        if (inviteCode.matches("^[A-Z0-9]{7}$")) {
            inviteCode = inviteCode.substring(0, 3) + "-" + inviteCode.substring(3);
        }
        if (!inviteCode.matches("^[A-Z0-9]{3}-[A-Z0-9]{4}$")) {
            throw new BusinessException(ErrorCode.BAD_REQUEST);
        }

        return inviteCode;
    }
}
