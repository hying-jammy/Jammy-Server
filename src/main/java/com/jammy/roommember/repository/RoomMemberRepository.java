package com.jammy.roommember.repository;

import com.jammy.roommember.domain.RoomMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomMemberRepository extends JpaRepository<RoomMember, Long> {
}
