package com.jammy.room.domain;

import com.jammy.global.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Room extends BaseEntity {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false, length = 50)
    private String title;

    @Column(name = "invite_code", nullable = false, unique = true, length = 20)
    private String inviteCode;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "member_limit", nullable = false)
    private Integer memberLimit;

    @Column(name = "time_capsule_open_at", nullable = false)
    private LocalDateTime timeCapsuleOpenAt;

    @Builder
    public Room(
            String title,
            String inviteCode,
            LocalDate startDate,
            LocalDate endDate,
            Integer memberLimit,
            LocalDateTime timeCapsuleOpenAt
    ) {
        this.title = title;
        this.inviteCode = inviteCode;
        this.startDate = startDate;
        this.endDate = endDate;
        this.memberLimit = memberLimit;
        this.timeCapsuleOpenAt = timeCapsuleOpenAt;
    }
}
