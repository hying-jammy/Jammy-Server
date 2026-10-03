package com.jammy.roommember.domain;

import com.jammy.global.domain.BaseEntity;
import com.jammy.room.domain.Room;
import com.jammy.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(
        name = "room_member",
        uniqueConstraints = @UniqueConstraint(columnNames = {"room_id", "user_id"})
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoomMember extends BaseEntity {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Builder
    public RoomMember(
            Room room,
            User user
    ) {
        this.room = room;
        this.user = user;
    }
}
