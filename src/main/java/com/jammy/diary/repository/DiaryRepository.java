package com.jammy.diary.repository;

import com.jammy.diary.domain.Diary;
import com.jammy.diary.domain.DiaryType;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface DiaryRepository extends JpaRepository<Diary, Long> {

    @EntityGraph(attributePaths = "user")
    List<Diary> findByRoomIdOrderByIdDesc(Long roomId);

    // 타임캡슐 일기 목록: 작성 순서(id 오름차순)
    @EntityGraph(attributePaths = "user")
    List<Diary> findByRoomIdAndTypeOrderByIdAsc(
            Long roomId,
            DiaryType TIME_CAPSULE
    );

    @Query("select distinct d.user.id from Diary d " +
            "where d.room.id = :roomId and d.type = com.jammy.diary.domain.DiaryType.TIME_CAPSULE")
    Set<Long> findCapsuleWriterIds(@Param("roomId") Long roomId);
}