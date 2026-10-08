package com.jammy.room.repository;

import com.jammy.room.domain.Room;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long> {
    boolean existsByInviteCode(String inviteCode);

    Optional<Room> findRoomByInviteCode(String inviteCode);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Room> findByInviteCode(String inviteCode);
}
