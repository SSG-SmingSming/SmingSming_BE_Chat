package com.smingsming.chat.domain.room.domain;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "room")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Room {
    @Id
    private String id;

    private String name;
    private String password;
    private boolean isLock;

    @CreatedDate
    private LocalDateTime createDate;

    private String roomThumbnail;
    private Long userId;
    private Long playlistId;

    @Builder
    public Room(String name, String password, boolean isLock, LocalDateTime createDate, String roomThumbnail, Long userId, Long playlistId) {
        this.name = name;
        this.password = password;
        this.isLock = isLock;
        this.createDate = createDate;
        this.roomThumbnail = roomThumbnail;
        this.userId = userId;
        this.playlistId = playlistId;
    }
}
