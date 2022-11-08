package com.smingsming.chat.domain.chat.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoomAddReqVo {
    private String name;
    private String password;
    private boolean isLock;
    private Long playlistId;
}
