package com.jammy.roommember.repository;

import com.jammy.roommember.domain.RoomMember;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RoomMemberRepository extends JpaRepository<RoomMember, Long> {
    boolean existsByRoomIdAndUserId(Long roomId, Long userId);
    long countByRoomId(Long roomId);

    @EntityGraph(attributePaths = "user")
    List<RoomMember> findByRoomIdOrderByIdAsc(Long roomId);

    @EntityGraph(attributePaths = "room")
    List<RoomMember> findByUserIdOrderByRoomStartDateDescRoomIdDesc(Long userId);

    @Query("select rm.room.id as roomId, count(rm) as memberCount "
            + "from RoomMember rm where rm.room.id in :roomIds group by rm.room.id")
    List<MemberCount> countMembersByRoomIdIn(List<Long> roomIds);

    interface MemberCount {
        Long getRoomId();
        Long getMemberCount();
    }
}
