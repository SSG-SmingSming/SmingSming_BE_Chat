package com.smingsming.chat.domain.room.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ResponseRoomDto {
    String id;
    String name;
    String password;
    boolean isLock;
    LocalDateTime createDate;
    String roomThumbnail;
    Long userId;
    Long playlistId;
}
