package com.jammy.roommember.repository;

import com.jammy.roommember.domain.RoomMember;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomMemberRepository extends JpaRepository<RoomMember, Long> {
    boolean existsByRoomIdAndUserId(Long roomId, Long userId);
    long countByRoomId(Long roomId);

    @EntityGraph(attributePaths = "user")
    List<RoomMember> findByRoomIdOrderByIdAsc(Long roomId);
}
