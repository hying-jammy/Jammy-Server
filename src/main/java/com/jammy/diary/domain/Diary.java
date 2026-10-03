package com.jammy.diary.domain;

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
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Diary extends BaseEntity {

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

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "image_url", length = 500)
    private String imageUrl;

    @Column(name = "type", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private DiaryType type;

    @Builder
    public Diary(
            Room room,
            User user,
            String content,
            String imageUrl,
            DiaryType type
    ) {
        this.room = room;
        this.user = user;
        this.content = content;
        this.imageUrl = imageUrl;
        this.type = type;
    }
}
