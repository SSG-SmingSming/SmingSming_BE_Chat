package com.smingsming.chat.domain.chat.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomVo {
    private String roomId;
    private String roomName;
    private int participant;
    private boolean isLock;
    private String userId;
    private String userName;
    private String userThumbnail;
    private Long playlistId;
    private String playlistName;
    private String playlistThumbnail;
}
