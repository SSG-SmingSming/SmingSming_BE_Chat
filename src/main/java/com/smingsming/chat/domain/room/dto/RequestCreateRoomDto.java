package com.smingsming.chat.domain.room.dto;

import lombok.Data;

@Data
public class RequestCreateRoomDto {
    String name;
    String password;
    boolean isLock;
    String roomThumbnail;
    Long userId;
    Long playlistId;
}
